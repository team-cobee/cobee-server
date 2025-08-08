package org.cobee.server.auth.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.SocialType;

@Getter
@Builder
public class MemberInfoDto {
    private Long id;
    private String name;
    private String email;
    private String birthDate;
    private String gender;
    private SocialType socialType;
    private Boolean isCompleted;
    private Boolean ocrValidation;
    private Boolean isHost;

    public static MemberInfoDto from(Member member) {
        return MemberInfoDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .birthDate(member.getBirthDate())
                .gender(member.getGender())
                .socialType(member.getSocialType())
                .isCompleted(member.getIsCompleted())
                .ocrValidation(member.getOcrValidation())
                .isHost(member.getIsHost())
                .build();
    }
}
