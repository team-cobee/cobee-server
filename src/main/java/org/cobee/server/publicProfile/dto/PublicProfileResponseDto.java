package org.cobee.server.publicProfile.dto;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.member.domain.Member;

public record PublicProfileResponseDto(
        Long userId,
        String name,
        String gender,
        String info,
        Lifestyle mLifestyle,
        Personality mPersonality,
        Boolean mSmoking,
        Boolean mSnoring,
        Boolean mPet
) {

    public static PublicProfileResponseDto from(PublicProfile publicProfile, Member member){
        return new PublicProfileResponseDto(
                member.getId(),
                member.getName(),
                member.getGender(),
                publicProfile.getInfo(),
                publicProfile.getLifestyle(),
                publicProfile.getPersonality(),
                publicProfile.getIsSmoking(),
                publicProfile.getIsSnoring(),
                publicProfile.getHasPet()
        );
    }

}
