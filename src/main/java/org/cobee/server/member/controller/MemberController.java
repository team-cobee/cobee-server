package org.cobee.server.member.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.fcm.FcmTokenRequest;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

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
}