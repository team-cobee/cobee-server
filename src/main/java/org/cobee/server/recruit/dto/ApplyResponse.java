package org.cobee.server.recruit.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.recruit.domain.ApplyRecord;

import java.time.LocalDate;

@Builder
@Getter
public class ApplyResponse {
    private Long appliedId;
    private Long postId;
    private Long fromMemberId;
    private Boolean isMatched;
    // 날짜는 굳이겠지??

    public static ApplyResponse from(ApplyRecord apply){
        return ApplyResponse.builder()
                .appliedId(apply.getId())
                .postId(apply.getPost().getId())
                .fromMemberId(apply.getMember().getId())
                .isMatched(apply.getIsMatched())
                .build();
    }
}
