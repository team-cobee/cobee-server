package org.cobee.server.chat.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.domain.RecruitPost;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int currentUserCount = 0;

    @Column(nullable = false)
    private int maxMemberCount;

    @OneToMany(mappedBy = "chatRoom")
    private Set<Member> users = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "post_id")
    private RecruitPost post;

    public ChatRoom(String name, int maxUserCount) {
        this.name = name;
        this.maxMemberCount = maxUserCount;
    }

    public int getCurrentUserCount() {
        return users.size();
    }

    public void editChatroomName(String name) {
        this.name = name;
    }

    public void addUser(Member user) {
        if (users.contains(user)) {
            return;
        }
        if (getCurrentUserCount() >= maxMemberCount) {
            throw new CustomException(ErrorCode.CHAT_ROOM_FULL);
        }
        users.add(user);
        user.join(this);
        currentUserCount++;
    }

    public void removeUser(Member user) {
        if (!users.contains(user)) {
            return;
        }
        users.remove(user);
        user.leave();
        currentUserCount--;
    }
}
