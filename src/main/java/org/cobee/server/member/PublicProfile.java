package org.cobee.server.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.common.Enum.Gender;
import org.cobee.server.common.Enum.Lifecycle;
import org.cobee.server.common.Enum.Personality;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PublicProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(columnDefinition = "TEXT")
    private String info;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifecycle m_lifestyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality m_personality;

    @Column
    private Boolean m_smoking;

    @Column
    private Boolean m_snoring;

    @Column
    private Boolean m_pet;

    @OneToOne
    private Member member;

    /* 알람id FK 이건 어떻게 해야하나?? */

}
