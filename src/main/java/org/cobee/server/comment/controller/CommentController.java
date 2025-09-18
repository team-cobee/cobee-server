package org.cobee.server.comment.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.comment.dto.CommentRequest;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.comment.service.CommentService;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                      @PathVariable(name="postId") Long postId,
                                                      @RequestBody CommentRequest request){

        try {
            Long memberId = principalDetails.getMember().getId();
            CommentResponse response = commentService.createComment(memberId,postId,request);
            return ApiResponse.success("댓글 생성 완료", "COMMENT_CREATED", response);
        } catch (CustomException e){
            return ApiResponse.failure("댓글 생성 실패", "COMMENT_FAILED", e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                          @PathVariable(name = "postId") Long postId){
        Long memberId = principalDetails.getMember().getId();
        List<CommentResponse> result = commentService.getAllComments(memberId, postId);
        return ApiResponse.success(postId+"의 모든 댓글 조회 완료", "COMMENT_ALL_VIEW_SUCCESS", result);
    }

    @PatchMapping("/{commentId}")
    public ApiResponse<CommentResponse> editComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @PathVariable(name = "commentId") Long commentId,
                                                    @RequestBody CommentRequest request){
        Long memberId = principalDetails.getMember().getId();
        CommentResponse result = commentService.updateComment(memberId,commentId,request);
        return ApiResponse.success("댓글 수정 완료", "COMMENT_EDIT_SUCCESS", result);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<Boolean> deleteComment(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                              @PathVariable(name = "commentId") Long commentId) {
        Long memberId = principalDetails.getMember().getId();
        Boolean result = commentService.deleteComment(memberId, commentId);
        if (result){
            return ApiResponse.success("성공", "COMMENT_DELETED", result);
        } else {
            return ApiResponse.failure("실패","COMMENT_DELETED_FAILED","comment delete failed", result);
        }
    }
}
