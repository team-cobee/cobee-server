package org.cobee.server.member.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.fcm.FcmTokenRequest;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.service.MemberService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/fcm/{memberId}")
    public ApiResponse<String> registerFcmToken(
            @PathVariable(name="memberId") Long memberId,
            @RequestBody FcmTokenRequest request
    ) {
        String fcm = memberService.updateFcmToken(memberId, request);
        return ApiResponse.success("fcm 전송 성공", "FCM-001",fcm);
    }

    @PostMapping("/profile/image")
    public ApiResponse<String> uploadProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) throws IOException {
        Long memberId = principalDetails.getMember().getId();
        String imageUrl = memberService.updateProfileImage(file, memberId);
        return ApiResponse.success("프로필 이미지 업데이트 완료", "PROFILE_IMAGE_UPDATED", imageUrl);
    }
}

