package org.cobee.server.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.Enum.Gender;
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
    private Lifestyle m_lifestyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality m_personality;

    @Column
    private Boolean m_smoking;

    @Column
    private Boolean m_snoring;

    @Column
    private Boolean m_pet;

    @Column
    private int cohabitant_count;

    @OneToOne
    private Member member;


}
