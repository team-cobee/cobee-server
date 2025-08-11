package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.RecruitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruits")
public class RecruitController {

    private final RecruitService recruitService;
    @PostMapping("/{memberId}")
    public ApiResponse<RecruitResponse> createRecruitPost(@RequestBody RecruitRequest request, @PathVariable(name="memberId") Long memberId){
        RecruitResponse result = recruitService.createRecruitPost(request, memberId);
        return ApiResponse.success("구인글 생성 완료", "RECRUIT_CREATED", result);
    }

    @PutMapping("/{memberId}/{postId}")
    public ApiResponse<RecruitResponse> updateRecruitPost(@RequestBody RecruitRequest request, @PathVariable(name="memberId") Long memberId, @PathVariable(name="postId") Long postId){
        RecruitResponse result = recruitService.updateRecruitPost(request,postId,memberId);
        return ApiResponse.success("구인글 수정 완료", "RECRUIT_UPDATED", result);
    }

    @GetMapping("/{postId}")
    public ApiResponse<RecruitResponse> getRecruitPost(@PathVariable(name="postId") Long postId){
        RecruitResponse result = recruitService.getRecruitPost(postId);
        return ApiResponse.success("postId가 "+postId+"인 post 조회 완료", "RECRUIT_GET_ONE", result);
    }

    @GetMapping("")
    public ApiResponse<List<RecruitResponse>> getRecruitPosts(){
        List<RecruitResponse> result = recruitService.getAllRecruitPosts();
        return ApiResponse.success("모든 구인글 조회 완료","RECRUIT_GET_ALL",result);
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Boolean> deleteRecruitPost(@PathVariable(name="postId") Long postId){
        Boolean result = recruitService.deleteRecruitPost(postId);
        if (result) {
            return ApiResponse.success("postId가 "+postId+"인 구인글 삭제 완료", "RECRUIT_DELETED", result);
        } else {
            return ApiResponse.failure("postId가 "+postId+"인 구인글 삭제 실패", "RECRUIT_DELETED_FAILED", ErrorCode.POST_NOT_FOUND.getMessage(), result);
        }
    }
}
