package org.cobee.server.recruit.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.recruit.domain.ApplyRecord;

@Builder
@Getter
public class ApplicantResponse {
    private String memberName;
    private Long profileId;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String birthDate;
    private Long applyId;

    public static ApplicantResponse from (Member member, PublicProfile profile, ApplyRecord record) {
        return ApplicantResponse.builder()
                .applyId(record.getId())
                .profileId(profile.getId())
                .memberName(record.getMember().getName())
                .gender(Gender.valueOf(member.getGender()))
                .birthDate(member.getBirthDate())
                .build();
    }

}
