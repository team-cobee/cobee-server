package org.cobee.server.alarm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.Enum.AlarmType;
import org.cobee.server.member.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column
    private int whereFrom;

    @ManyToOne
    @JoinColumn(name="from_user_id")
    private Member member;


}
