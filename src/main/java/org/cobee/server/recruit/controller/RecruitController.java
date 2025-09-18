package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.RecruitService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recruits")
@Slf4j
public class RecruitController {

    private final RecruitService recruitService;

    @PostMapping("")
    public ApiResponse<RecruitResponse> createRecruitPost(@RequestBody RecruitRequest request,
                                                          @AuthenticationPrincipal PrincipalDetails principalDetails){
        try{
            Long memberId = principalDetails.getMember().getId();
            RecruitResponse result = recruitService.createRecruitPost(request, memberId);
            return ApiResponse.success("구인글 생성 완료", "RECRUIT_CREATED", result);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @PutMapping("/{postId}")
    public ApiResponse<RecruitResponse> updateRecruitPost(@RequestBody RecruitRequest request,
                                                          @AuthenticationPrincipal PrincipalDetails principalDetails,
                                                          @PathVariable(name = "postId") Long postId) {
        Long memberId = principalDetails.getMember().getId();
        RecruitResponse result = recruitService.updateRecruitPost(request, postId, memberId);
        return ApiResponse.success("구인글 수정 완료", "RECRUIT_UPDATED", result);
    }

    @GetMapping("/{postId}")
    public ApiResponse<RecruitResponse> getRecruitPost(@PathVariable(name = "postId") Long postId) {
        RecruitResponse result = recruitService.getRecruitPost(postId);
        return ApiResponse.success("postId가 " + postId + "인 post 조회 완료", "RECRUIT_GET_ONE", result);
    }

    @GetMapping("")
    public ApiResponse<List<RecruitResponse>> getRecruitPosts(@AuthenticationPrincipal PrincipalDetails principalDetails){
        try{
            List<RecruitResponse> result = recruitService.getAllRecruitPosts();
            return ApiResponse.success("모든 구인글 조회 완료","RECRUIT_GET_ALL",result);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<Boolean> deleteRecruitPost(@PathVariable(name = "postId") Long postId) {
        Boolean result = recruitService.deleteRecruitPost(postId);
        if (result) {
            return ApiResponse.success("postId가 " + postId + "인 구인글 삭제 완료", "RECRUIT_DELETED", result);
        } else {
            return ApiResponse.failure("postId가 " + postId + "인 구인글 삭제 실패", "RECRUIT_DELETED_FAILED",
                    ErrorCode.POST_NOT_FOUND.getMessage(), result);
        }
    }

    @GetMapping("/my")
    public ApiResponse<List<RecruitResponse>> getMyRecruitPosts(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getId();
        List<RecruitResponse> result = recruitService.getAllMyPost(memberId);
        return ApiResponse.success("나의 모든 구인글 조회 완료", "MY_RECRUIT_VIEWED", result);
    }

    @PostMapping("/{postId}/images")
    public ApiResponse<List<String>> uploadRecruitImages(
            @RequestParam("files") MultipartFile[] files,
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getId();
        List<String> imageUrls = recruitService.addImages(files, postId, memberId);
        return ApiResponse.success("구인글 이미지 업로드 완료", "RECRUIT_IMAGES_UPLOADED", imageUrls);
    }

}
