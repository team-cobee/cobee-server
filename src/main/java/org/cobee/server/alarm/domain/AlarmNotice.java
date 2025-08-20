package org.cobee.server.alarm.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.domain.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AlarmNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean isRead;

    @ManyToOne
    @JoinColumn(name="to_user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="alarm_id")
    private Alarm alarm;

    public void updateIsRead(Boolean isReadCheck){
        this.isRead=isReadCheck;
    }

}