package org.cobee.server.member.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.member.domain.UserPreferences;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Getter
@Builder
public class UserPreferencesResponseDto {

  private Long id;
  private Gender preferredGender;
  private String additionalInfo;
  private Lifestyle lifestyle;
  private Personality personality;
  private Boolean smokingPreference;
  private Boolean snoringPreference;
  private Integer cohabitantCount;
  private Boolean petPreference;


  public static UserPreferencesResponseDto from(UserPreferences userPreferences) {
    return UserPreferencesResponseDto.builder()
        .id(userPreferences.getId())
        .preferredGender(userPreferences.getGender())
        .lifestyle(userPreferences.getLifestyle())
        .personality(userPreferences.getPersonality())
        .smokingPreference(userPreferences.getIsSmoking())
        .snoringPreference(userPreferences.getIsSnoring())
        .cohabitantCount(userPreferences.getCohabitantCount())
        .petPreference(userPreferences.getHasPet())
        .additionalInfo(userPreferences.getInfo())
        .build();
  }
}