package org.cobee.server.recruit.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

import java.util.ArrayList;
import java.util.List;

/*
TODO
- response에 댓글개수, 조회수(가능하면), 지원자 n명 추가
 */
@Builder
@Getter
public class RecruitResponse{
    private Long postId;
    private String authorName;
    private Float location; /* 이거 어케 처리할지 고민 */
    private String profileUrl;
    private String title;
    private int recruitCount;
    private int rentalCost;
    private int monthlyCost;
    private String content;
    private List<CommentResponse> comments;

    public static RecruitResponse from(RecruitPost post, Member member) {
        List<CommentResponse> responses = new ArrayList<>();
        List<Comment> result = post.getComments();
        for (Comment comment : result) {
            responses.add(CommentResponse.from(member, comment));
        }

        return RecruitResponse.builder()
                .postId(post.getId())
                .authorName(post.getMember().getName())
                .location(post.getDistance())
                .profileUrl(post.getMember().getProfileUrl())
                .title(post.getTitle())
                .recruitCount(post.getRecruitCount())
                .rentalCost(post.getRentCost())
                .monthlyCost(post.getMonthlyCost())
                .content(post.getContent())
                .comments(responses)
                .build();
    }

}