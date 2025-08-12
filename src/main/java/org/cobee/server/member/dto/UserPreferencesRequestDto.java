package org.cobee.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.enums.PreferredGender;
import org.cobee.server.member.domain.enums.SmokingPreference;
import org.cobee.server.member.domain.enums.SnoringPreference;
import org.cobee.server.member.domain.enums.PetPreference;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesRequestDto {

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
}