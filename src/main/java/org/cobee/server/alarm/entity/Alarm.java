package org.cobee.server.alarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.alarm.Enum.AlarmType;
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
    private int where_from; // 왜 int??

    @ManyToOne
    @JoinColumn(name="from_user_id")
    private Member member;


}
