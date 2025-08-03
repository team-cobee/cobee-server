package org.cobee.server.recruit.dto;

import lombok.Builder;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;
@Builder
public record RecruitResponse(
        String authorName, Float location /* 이거 어케 처리할지 고민 */, String profileUrl,
        String title, int recruitCount, int rentalCost, int monthlyCost,
        String content //String formUrl
) {

    public static RecruitResponse from(RecruitPost post, Member member) {
        return RecruitResponse.builder()
                .authorName(member.getName())
                .location(post.getDistance())
                .profileUrl(member.getProfileUrl())
                .title(post.getTitle())
                .recruitCount(post.getRecruitCount())
                .rentalCost(post.getRentCost())
                .monthlyCost(post.getMonthlyCost())
                .content(post.getContent())
                .build();

    }
}
