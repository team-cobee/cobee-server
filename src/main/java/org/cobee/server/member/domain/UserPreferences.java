package org.cobee.server.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


}
