package org.cobee.server.publicProfile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_profile_id")
    private Long id;

    private String info;

    @Enumerated(EnumType.STRING)
    private Lifestyle mLifestyle;

    @Enumerated(EnumType.STRING)
    private Personality mPersonality;

    private Boolean mSmoking;

    private Boolean mSnoring;

    private Boolean mPet;

//    @OneToOne
//    private Member member;

    public PublicProfile(String info, Lifestyle mLifestyle, Personality mPersonality, Boolean mSmoking, Boolean mSnoring, Boolean mPet) {
        this.info = info;
        this.mLifestyle = mLifestyle;
        this.mPersonality = mPersonality;
        this.mSmoking = mSmoking;
        this.mSnoring = mSnoring;
        this.mPet = mPet;
    }
    public void update(String info, Lifestyle mLifestyle, Personality mPersonality, Boolean mSmoking, Boolean mSnoring, Boolean mPet) {
        this.info = info;
        this.mLifestyle = mLifestyle;
        this.mPersonality = mPersonality;
        this.mSmoking = mSmoking;
        this.mSnoring = mSnoring;
        this.mPet = mPet;
    }
}
