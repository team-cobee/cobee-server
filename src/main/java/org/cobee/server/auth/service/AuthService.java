package org.cobee.server.auth.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.handler.HttpCookieOAuth2AuthorizationRequestRepository;
import org.cobee.server.auth.jwt.JwtTokenProvider;
import org.cobee.server.auth.jwt.TokenInfo;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.SocialType;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        String token = extractTokenFromRequest(request);

        try {
            Claims claims = jwtTokenProvider.parseClaims(token);
            long memberId = Long.parseLong(claims.getSubject());
            jwtTokenProvider.deleteRefreshToken(memberId);
            log.info("Logout successful for member id {}", memberId);
        } catch (Exception e) {
            log.error("logout 중 토큰 처리 실패: {}", e.getMessage());
            throw new CustomException(ErrorCode.LOGOUT_TOKEN_PROCESSING_FAILED);
        }

    }

    public void withdrawMember(Long memberId, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        
        SocialType socialType = member.getSocialType();
        String socialId = member.getSocialId();

        jwtTokenProvider.deleteRefreshToken(memberId);
        memberRepository.delete(member);
        
        if (socialType != null) {
            log.info("소셜 회원 탈퇴 완료 - socialType: {}, socialId: {}", socialType, socialId);
        } else {
            log.info("일반 회원 탈퇴 완료 - memberId: {}", memberId);
        }
    }

    public TokenInfo reissueToken(String refreshToken) {
        return jwtTokenProvider.reissueAccessToken(refreshToken);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new CustomException(ErrorCode.BEARER_TOKEN_HAS_ISSUE);
    }
}