package org.cobee.server.comment.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.comment.dto.CommentRequest;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.comment.service.CommentService;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{memberId}/posts/{postId}/comments")
    public ApiResponse<CommentResponse> createComment(@PathVariable(name="memberId") Long memberId,
                                                      @PathVariable(name="postId") Long postId,
                                                      @RequestBody CommentRequest request){

        try {
            CommentResponse response = commentService.createComment(memberId,postId,request);
            return ApiResponse.success("댓글 생성 완료", "COMMENT_CREATED", response);
        } catch (CustomException e){
            return ApiResponse.failure("댓글 생성 실패", "COMMENT_FAILED", e.getMessage());
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ApiResponse<List<CommentResponse>> getComments(Long memberId, @PathVariable(name = "postId") Long postId){
        List<CommentResponse> result = commentService.getAllComments(memberId, postId);
        return ApiResponse.success(postId+"의 모든 댓글 조회 완료", "COMMENT_ALL_VIEW_SUCCESS", result);
    }
}
