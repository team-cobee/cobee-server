package org.cobee.server.recruit.dto;

import lombok.Builder;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public record RecruitResponse(
        Long id, String authorName, Float location /* 이거 어케 처리할지 고민 */, String profileUrl,
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
                .id(post.getId())
                .authorName(member.getName())
                .location(post.getDistance())
                .profileUrl(member.getProfileUrl())
                .title(post.getTitle())
                .recruitCount(post.getRecruitCount())
                .rentalCost(post.getRentCost())
                .monthlyCost(post.getMonthlyCost())
                .content(post.getContent())
                .comments(responses)
                .build();

    }
}
