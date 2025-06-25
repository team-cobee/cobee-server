package org.cobee.server.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.Enum.SocialType;
import org.cobee.server.alarm.Alarm;
import org.cobee.server.alarm.AlarmNotice;
import org.cobee.server.chat.ChattingRoom;
import org.cobee.server.comment.Comment;
import org.cobee.server.recruit.ApplyRecord;
import org.cobee.server.recruit.RecruitPost;

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
    private String birth_date;

    @Column
    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Column
    private Boolean ocr_validation;

    @Column
    private Boolean is_host;

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

    @OneToOne
    private PublicProfile publicProfile;






}
