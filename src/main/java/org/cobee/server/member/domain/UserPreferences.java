package org.cobee.server.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.global.BaseEntity;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserPreferences extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // 선호 성별
    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    private Boolean isSmoking;

    @Column
    private Boolean isSnoring;

    @Column
    private Boolean hasPet;

    @Column
    private int cohabitantCount;

    @OneToOne
    private Member member;

  public void updatePreferences(Gender preferredGender, Lifestyle lifestyle,
      Personality personality, Boolean smokingPreference, Boolean snoringPreference,
      Integer cohabitantCount, Boolean petPreference,
      String additionalInfo) {
    this.gender = preferredGender;
    this.lifestyle = lifestyle;
    this.personality = personality;
    this.isSmoking = smokingPreference;
    this.isSnoring = snoringPreference;
    this.hasPet = petPreference;
    this.cohabitantCount = cohabitantCount;
    this.info = additionalInfo;
  }
}
