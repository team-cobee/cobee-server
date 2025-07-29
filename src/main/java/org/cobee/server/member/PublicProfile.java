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
    private Lifecycle lifestyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    private Boolean isSmoking;

    @Column
    private Boolean isSnoring;

    @Column
    private Boolean hasPet;

    @OneToOne
    private Member member;

}
