package org.cobee.server.alarm;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.Member;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AlarmNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean is_read;

    @ManyToOne
    @JoinColumn(name="to_user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="alarm_id")
    private Alarm alarm;

}
