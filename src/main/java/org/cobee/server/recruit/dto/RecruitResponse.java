package org.cobee.server.recruit.dto;

import lombok.Builder;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/*
TODO
- class로 바꾸기
- response에 댓글개수, 조회수(가능하면), 지원자 n명 추가
 */
@Builder
public record RecruitResponse(
        Long postId, String authorName, Float location /* 이거 어케 처리할지 고민 */, String profileUrl,
        String title, int recruitCount, int rentalCost, int monthlyCost,
        String content //String formUrl
        , List<CommentResponse> comments
) {

    public static RecruitResponse from(RecruitPost post, Member member) {
        List<CommentResponse> responses = new ArrayList<>();
        List<Comment> result = post.getComments();
        for (Comment comment : result) {
            responses.add(CommentResponse.from(member,comment));
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
