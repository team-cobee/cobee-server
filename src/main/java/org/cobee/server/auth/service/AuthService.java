package org.cobee.server.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.handler.HttpCookieOAuth2AuthorizationRequestRepository;
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

    public void logout(HttpServletRequest request, HttpServletResponse response) {
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        log.info("사용자 로그아웃 완료");
    }

    public void withdrawMember(Long memberId, HttpServletRequest request, HttpServletResponse response) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        
        SocialType socialType = member.getSocialType();
        String socialId = member.getSocialId();
        
        memberRepository.delete(member);
        
        if (socialType != null) {
            log.info("소셜 회원 탈퇴 완료 - socialType: {}, socialId: {}", socialType, socialId);
        } else {
            log.info("일반 회원 탈퇴 완료 - memberId: {}", memberId);
        }
    }
}