package org.cobee.server.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.enums.PreferredGender;
import org.cobee.server.member.domain.enums.SmokingPreference;
import org.cobee.server.member.domain.enums.SnoringPreference;
import org.cobee.server.member.domain.enums.PetPreference;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 선호 성별
    @Column
    @Enumerated(EnumType.STRING)
    private PreferredGender preferredGender;

    // 선호 나이대 (최소)
    @Column
    private Integer minAge;

    // 선호 나이대 (최대)
    @Column
    private Integer maxAge;

    // 생활 패턴
    @Column
    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    // 성격 유형
    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    // 흡연 선호도
    @Column
    @Enumerated(EnumType.STRING)
    private SmokingPreference smokingPreference;

    // 코골이 선호도
    @Column
    @Enumerated(EnumType.STRING)
    private SnoringPreference snoringPreference;

    // 동거 가능 인원 수 (최소)
    @Column
    private Integer minCohabitantCount;

    // 동거 가능 인원 수 (최대)
    @Column
    private Integer maxCohabitantCount;

    // 반려동물 선호도
    @Column
    @Enumerated(EnumType.STRING)
    private PetPreference petPreference;

    // 기타 정보
    @Column(columnDefinition = "TEXT")
    private String additionalInfo;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public void updatePreferences(PreferredGender preferredGender, Integer minAge, Integer maxAge, 
                                 Lifestyle lifestyle, Personality personality,
                                 SmokingPreference smokingPreference, SnoringPreference snoringPreference,
                                 Integer minCohabitantCount, Integer maxCohabitantCount,
                                 PetPreference petPreference, String additionalInfo) {
        this.preferredGender = preferredGender;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.lifestyle = lifestyle;
        this.personality = personality;
        this.smokingPreference = smokingPreference;
        this.snoringPreference = snoringPreference;
        this.minCohabitantCount = minCohabitantCount;
        this.maxCohabitantCount = maxCohabitantCount;
        this.petPreference = petPreference;
        this.additionalInfo = additionalInfo;
    }
}
