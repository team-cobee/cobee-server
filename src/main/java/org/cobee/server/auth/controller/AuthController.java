package org.cobee.server.auth.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.dto.MemberInfoDto;
import org.cobee.server.auth.dto.RefreshTokenRequest;
import org.cobee.server.auth.jwt.TokenInfo;
import org.cobee.server.auth.service.AuthService;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping()
    public ApiResponse<MemberInfoDto> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Member member = principalDetails.getMember();
        MemberInfoDto memberInfo = MemberInfoDto.from(member);
        return ApiResponse.success("사용자 정보 조회 성공", "200", memberInfo);
    }

    @PostMapping("/refresh")
    public ApiResponse<TokenInfo> refreshToken(@RequestBody RefreshTokenRequest request) {
        TokenInfo tokenInfo = authService.reissueToken(request.getRefreshToken());
        return ApiResponse.success("토큰 재발급 성공", "TOKEN_REFRESH_SUCCESS", tokenInfo);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ApiResponse.success("로그아웃 성공", "200", null);
    }

    @DeleteMapping("/withdraw")
    public ApiResponse<Void> withdraw(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                     HttpServletRequest request, HttpServletResponse response) {
        Member member = principalDetails.getMember();
        authService.withdrawMember(member.getId(), request, response);
        return ApiResponse.success("회원 탈퇴 성공", "200", null);
    }
}
