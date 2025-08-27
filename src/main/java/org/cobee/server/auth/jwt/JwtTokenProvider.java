package org.cobee.server.auth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.domain.RefreshToken;
import org.cobee.server.auth.repository.RefreshTokenRepository;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey,
                            @Value("${jwt.access-token-expire-time}")
                                    long accessTokenExpireTime,
                            @Value("${jwt.refresh-token-expire-time}")
                                    long refreshTokenExpireTime,
                            MemberRepository memberRepository,
                            RefreshTokenRepository refreshTokenRepository) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTokenExpireTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTokenExpireTime;
        this.memberRepository = memberRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public TokenInfo generateToken(Authentication authentication) {
        // PrincipalDetails에서 Member ID 추출
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성 (Member ID를 subject로 사용)
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenEntity = RefreshToken.of(
                member.getId(),
                refreshToken,
                REFRESH_TOKEN_EXPIRE_TIME / 1000
        );
        refreshTokenRepository.save(refreshTokenEntity);
        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new CustomException(ErrorCode.JWT_TOKEN_MISSING_AUTHORITIES);
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // 토큰의 subject(사용자 식별자)로 Member 조회
        String subject = claims.getSubject();
        Member member = memberRepository.findById(Long.parseLong(subject))
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // PrincipalDetails 객체를 만들어서 Authentication 리턴
        PrincipalDetails principalDetails = new PrincipalDetails(member, null);
        return new UsernamePasswordAuthenticationToken(principalDetails, "", authorities);
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            throw new CustomException(ErrorCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new CustomException(ErrorCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new CustomException(ErrorCode.UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            throw new CustomException(ErrorCode.JWT_CLAIMS_EMPTY);
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // RefreshToken으로 새 AccessToken 발급
    public TokenInfo reissueAccessToken(String refreshToken) {
        // RefreshToken 검증
        if (!validateToken(refreshToken))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        // Refreshtoken에서 memberId 추출
        Claims claims = parseClaims(refreshToken);
        long memberId = Long.parseLong((claims.getSubject()));

        // redis 에서 refreshToken 확인
        RefreshToken storedToken = refreshTokenRepository.findById(String.valueOf(memberId))
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

        if(!storedToken.getToken().equals(refreshToken))
            throw new CustomException(ErrorCode.REFRESH_TOKEN_MISMATCH);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 새 accessToken 생성
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String newAccessToken = Jwts.builder()
                .setSubject(String.valueOf(member.getId()))
                .claim(AUTHORITIES_KEY, "ROLE_USER")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }

    // logout (RefreshToken) 삭제
    public void deleteRefreshToken(Long memberId) {
        refreshTokenRepository.deleteById(String.valueOf(memberId));
        log.info("Refresh token deleted for memberId: {}", memberId);
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}