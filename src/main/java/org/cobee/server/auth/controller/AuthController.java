package org.cobee.server.auth.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.dto.MemberInfoDto;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/kakao")
    public ResponseEntity<ApiResponse> getKakaoUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        MemberInfoDto memberInfo = MemberInfoDto.from(member);
        return ResponseEntity.ok(ApiResponse.success("카카오 사용자 정보 조회 성공", "200", memberInfo));
    }

    @GetMapping("/google")
    public ResponseEntity<ApiResponse> getGoogleUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        MemberInfoDto memberInfo = MemberInfoDto.from(member);
        return ResponseEntity.ok(ApiResponse.success("구글 사용자 정보 조회 성공", "200", memberInfo));
    }
}
