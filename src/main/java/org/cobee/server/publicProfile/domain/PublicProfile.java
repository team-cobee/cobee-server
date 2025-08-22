package org.cobee.server.publicProfile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.publicProfile.domain.enums.*;

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
    private Lifestyle lifestyle;

    @Enumerated(EnumType.STRING)
    private Personality personality;

    private Smoking isSmoking;

    private Snoring isSnoring;

    private Pets hasPet;

//    @OneToOne
//    private Member member;

    public PublicProfile(String info, Lifestyle lifestyle, Personality personality, Smoking isSmoking, Snoring isSnoring, Pets hasPet) {
        this.info = info;
        this.lifestyle = lifestyle;
        this.personality = personality;
        this.isSmoking = isSmoking;
        this.isSnoring = isSnoring;
        this.hasPet = hasPet;
    }
    public void update(String info, Lifestyle lifestyle, Personality personality, Smoking isSmoking, Snoring isSnoring, Pets hasPet) {
        this.info = info;
        this.lifestyle = lifestyle;
        this.personality = personality;
        this.isSmoking = isSmoking;
        this.isSnoring = isSnoring;
        this.hasPet = hasPet;
    }
}
