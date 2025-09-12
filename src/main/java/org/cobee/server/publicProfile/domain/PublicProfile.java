package org.cobee.server.publicProfile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.global.BaseEntity;
import org.cobee.server.member.domain.Member;
import org.cobee.server.publicProfile.domain.enums.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "public_profile_id")
    private Long id;

    private String info;

    @Enumerated(EnumType.STRING)
    private Lifestyle lifestyle;

    @Enumerated(EnumType.STRING)
    private Personality personality;
    @Enumerated(EnumType.STRING)
    private Smoking isSmoking;
    @Enumerated(EnumType.STRING)
    private Snoring isSnoring;
    @Enumerated(EnumType.STRING)
    private Pets hasPet;

    // 생각해보니 이미 Member에 OneToOne으로 매핑되어 있기에 주석 처리했던 것 같다..
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
