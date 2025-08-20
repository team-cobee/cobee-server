package org.cobee.server.member.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.alarm.domain.AlarmNotice;
import org.cobee.server.chat.domain.ChatRoom;
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
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column
    private String birthDate;

    @Column
    private String gender;

    @Enumerated(EnumType.STRING)
    //@Column(nullable = false)
    private SocialType socialType;

    // 소셜 플랫폼에서 제공하는 사용자 고유 ID (카카오, 구글 등)
    //@Column(nullable = false)
    private String socialId;

    // 회원가입 완료 여부 (소셜 로그인 후 추가 정보 입력 완료 여부)
    @Column(nullable = false)
    private Boolean isCompleted;

    // OCR 주민등록증 인증 여부
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "public_profile_id")
    private PublicProfile publicProfile;

    public Member(String username) {
        this.name = username;
    }

    public void setPublicProfile(PublicProfile publicProfile) {
        this.publicProfile = publicProfile;
    }

    public void join(ChatRoom room) {
        this.chatRoom = room;
    }

    public void leave() {
        this.chatRoom = null;
    }

    public Member update(String name, String email) {
        this.name = name;
        this.email = email;
        return this;
    }

    public void updateOcrValidation(String realName, String birthDate, String gender) {
        this.name = realName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.ocrValidation = true;
    }

    public String updateFcmToken(String fcmToken) {
        this.fcmToken=fcmToken;
        return fcmToken;
    }
}

