package org.cobee.server.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.Enum.Gender;
import org.cobee.server.Enum.Lifecycle;
import org.cobee.server.Enum.Personality;

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
    private Lifecycle lifestyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    private Boolean smoking;

    @Column
    private Boolean snoring;

    @Column
    private Boolean pet;

    @Column
    private int cohabitantCount;

    @OneToOne
    private Member member;


}
