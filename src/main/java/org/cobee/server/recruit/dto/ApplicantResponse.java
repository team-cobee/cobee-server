package org.cobee.server.recruit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.enums.MatchStatus;

@Builder
@Getter
public class ApplicantResponse {
    private String memberName;
    private Long publicProfileId;
    private Gender gender;
    private String birthDate;
    private Long applyId;
    private MatchStatus matchStatus;

    public static ApplicantResponse from (Member member, ApplyRecord record) {
        return ApplicantResponse.builder()
                .applyId(record.getId())
                .publicProfileId(member.getPublicProfile().getId())
                .memberName(record.getMember().getName())
                .gender(Gender.valueOf(record.getMember().getGender()))
                .birthDate(record.getMember().getBirthDate())
                .matchStatus(record.getIsMatched())
                .build();
    }

}
