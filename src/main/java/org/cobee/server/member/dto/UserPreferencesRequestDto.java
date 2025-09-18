package org.cobee.server.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesRequestDto {

  private Gender preferredGender;
  private String additionalInfo;
  private Lifestyle lifestyle;
  private Personality personality;
  private Boolean smokingPreference;
  private Boolean snoringPreference;
  private Integer CohabitantCount;
  private Boolean petPreference;
}
