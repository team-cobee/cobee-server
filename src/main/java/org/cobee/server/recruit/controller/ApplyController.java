package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.dto.ApplyAcceptRequest;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.ApplyService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping("/{fromMemberId}")
    public ApiResponse<ApplyResponse> applyForRecruit(@PathVariable(name="fromMemberId") Long memberId,
                                                      @RequestBody ApplyRequest request){
        ApplyResponse result = applyService.apply(memberId, request);
        return ApiResponse.success("지원이 완료되었습니다", "APPLY-001", result);

    }

    @PostMapping("/accept/{applyId}")  // 글쓴이와 현재로그인한 사람 같은지 비교 안해도 되려나? 지원목록만 쓴 글이 있을때 보여줌 돼
    public ApiResponse<ApplyResponse> acceptApply(@PathVariable(name="applyId") Long applyId,
                                                  @RequestBody ApplyAcceptRequest request){
        ApplyResponse result = applyService.accept(applyId, request);
        if (request.getIsAccept()){
            return ApiResponse.success("매칭이 되었습니다.", "APPLY-002", result);
        } else {
            return ApiResponse.success("매칭이 거절되었습니다", "APPLY-003", result);
        }
    }

    @GetMapping("/my")
    public ApiResponse<List<RecruitResponse>> myAppliedPost(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        // 현재 로그인 사용자
        Long memberId = principalDetails.getMember().getId();
        List<RecruitResponse> myApplies = applyService.getMyApplies(memberId);
        return ApiResponse.success("나의 지원 구인글 목룍 조회 완료", "APPLY-004", myApplies);
    }


}
