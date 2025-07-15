package org.cobee.server.chat;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.member.Member;
import org.cobee.server.recruit.RecruitPost;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChattingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int memberCount;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private List<Member> members;

    @OneToOne
    @JoinColumn(name="post_id")
    private RecruitPost post;

    /* 알람 id는 어떻게 구현? 직접적으로 연결이 안되어있는데 */
}
