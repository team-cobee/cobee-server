package org.cobee.server.alarm.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;
import org.cobee.server.member.domain.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmSourceType sourceType; // COMMENT / DIARY / CHATROOM

    @Column(nullable = false)
    private Long sourceId;

    @ManyToOne
    @JoinColumn(name="from_user_id")
    private Member member;


}