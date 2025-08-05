package org.cobee.server.comment.dto;


import lombok.Builder;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.member.domain.Member;

@Builder
public record CommentResponse(Long commentId, Long parentId, String content, String nickname, String profileImg) {

    public static CommentResponse from(Member member, Comment comment) {
        Long parentId = comment.getParent() != null ? comment.getParent().getId() : null;

        return CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(member.getName())
                .parentId(parentId)
                .profileImg(member.getProfileUrl())
                .build();
    }
}
