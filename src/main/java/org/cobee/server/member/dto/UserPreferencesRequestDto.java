package org.cobee.server.member.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
  @NotNull(message = "동거인 수는 필수입니다")
  @Min(value = 2, message = "동거인 수는 최소 2명입니다")
  @Max(value = 10, message = "동거인 수는 최대 10명입니다")
  private Integer cohabitantCount;
  private Boolean petPreference;
}
