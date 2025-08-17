package org.cobee.server.recruit.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.recruit.domain.ApplyRecord;

@Builder
@Getter
public class ApplyResponse {
    private Long id;
    private Long appliedPostId;
    private Long appliedMemberId;
    private Boolean isMatched;

    public static ApplyResponse from(ApplyRecord apply) {
        return ApplyResponse.builder()
                .id(apply.getId())
                .appliedPostId(apply.getPost().getId())
                .appliedMemberId(apply.getMember().getId())
                .isMatched(apply.getIsMatched())
                .build();
    }
}
