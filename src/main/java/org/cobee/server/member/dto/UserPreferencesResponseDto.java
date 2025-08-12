package org.cobee.server.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.UserPreferences;
import org.cobee.server.member.domain.enums.PreferredGender;
import org.cobee.server.member.domain.enums.SmokingPreference;
import org.cobee.server.member.domain.enums.SnoringPreference;
import org.cobee.server.member.domain.enums.PetPreference;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Getter
@Builder
public class UserPreferencesResponseDto {

    private Long id;
    private PreferredGender preferredGender;
    private Integer minAge;
    private Integer maxAge;
    private Lifestyle lifestyle;
    private Personality personality;
    private SmokingPreference smokingPreference;
    private SnoringPreference snoringPreference;
    private Integer minCohabitantCount;
    private Integer maxCohabitantCount;
    private PetPreference petPreference;
    private String additionalInfo;

    public static UserPreferencesResponseDto from(UserPreferences userPreferences) {
        return UserPreferencesResponseDto.builder()
                .id(userPreferences.getId())
                .preferredGender(userPreferences.getPreferredGender())
                .minAge(userPreferences.getMinAge())
                .maxAge(userPreferences.getMaxAge())
                .lifestyle(userPreferences.getLifestyle())
                .personality(userPreferences.getPersonality())
                .smokingPreference(userPreferences.getSmokingPreference())
                .snoringPreference(userPreferences.getSnoringPreference())
                .minCohabitantCount(userPreferences.getMinCohabitantCount())
                .maxCohabitantCount(userPreferences.getMaxCohabitantCount())
                .petPreference(userPreferences.getPetPreference())
                .additionalInfo(userPreferences.getAdditionalInfo())
                .build();
    }
}