package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.RecruitService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;
    @PostMapping("/recruits/{memberId}")
    public ApiResponse<RecruitResponse> createRecruitPost(@RequestBody RecruitRequest request, @PathVariable(name="memberId") Long memberId){
        RecruitResponse result = recruitService.createRecruitPost(request, memberId);
        return ApiResponse.success("구인글 생성 완료", "RECRUIT_CREATED", result);
    }

    @PutMapping("/recruits/{memberId}/{postId}")
    public ApiResponse<RecruitResponse> updateRecruitPost(@RequestBody RecruitRequest request, @PathVariable(name="memberId") Long memberId, @PathVariable(name="postId") Long postId){
        RecruitResponse result = recruitService.updateRecruitPost(request,postId,memberId);
        return ApiResponse.success("구인글 수정 완료", "RECRUIT_UPDATED", result);
    }

    @GetMapping("/recruits/{postId}")
    public ApiResponse<RecruitResponse> getRecruitPost(@PathVariable(name="postId") Long postId){
        RecruitResponse result = recruitService.getRecruitPost(postId);
        return ApiResponse.success("postId가 "+postId+"인 post 조회 완료", "RECRUIT_GET_ONE", result);
    }
}
