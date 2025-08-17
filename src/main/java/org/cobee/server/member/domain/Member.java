package org.cobee.server.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.alarm.domain.AlarmNotice;
import org.cobee.server.chat.domain.ChattingRoom;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.member.domain.enums.SocialType;
import org.cobee.server.publicProfile.domain.PublicProfile;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.RecruitPost;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String birthDate;

    @Column
    private String gender;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private String profileUrl;

    @Column
    private Boolean ocrValidation;

    @Column
    private Boolean isHost;

    @Column
    private String fcmToken; // 앱 로그인 시 갱신 저장

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private UserPreferences userPreferences;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<ApplyRecord> applyRecords;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RecruitPost> recruitPosts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<AlarmNotice> alarmNotices;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Alarm> alarms;

    @ManyToOne
    @JoinColumn(name="chatroom_id")
    private ChattingRoom chattingRoom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "public_profile_id")
    private PublicProfile publicProfile;

    public void setPublicProfile(PublicProfile publicProfile) {
        this.publicProfile = publicProfile;
    }


    public String updateFcmToken(String fcmToken) {
        this.fcmToken=fcmToken;
        return fcmToken;
    }
}
