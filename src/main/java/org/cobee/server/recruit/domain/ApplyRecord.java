package org.cobee.server.recruit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.enums.MatchStatus;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ApplyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MatchStatus isMatched;

    @Column
    private LocalDate submittedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private RecruitPost post;

    /* 외래키 알람id는 어떻게 표현하지?? 직접적인 연결이 없는데 -> 최종적인 연결관계만 파악하고 이렇게 하면 돼*/
    @ManyToOne
    @JoinColumn(name = "alarm_id")  // FK 열, FK 연결하고 싶은 그 객체 타입을 쓰면 매핑해줌
    private Alarm alarm;

    public void setPost(RecruitPost recruitPost) {
        this.post=recruitPost;
    }

    public void acceptMatching(Boolean accept){
        if (accept) this.isMatched=MatchStatus.ACCEPTED;
        else this.isMatched=MatchStatus.REJECTED;
    }
}
