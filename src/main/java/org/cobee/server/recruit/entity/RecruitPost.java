package org.cobee.server.recruit.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.common.Enum.Lifecycle;
import org.cobee.server.common.Enum.Personality;
import org.cobee.server.common.Enum.RecruitStatus;
import org.cobee.server.chat.entity.ChattingRoom;
import org.cobee.server.comment.entity.Comment;
import org.cobee.server.member.Member;

import java.sql.Timestamp;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecruitPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private int recruit_count;

    @Column
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Column
    private int rent_cost;

    @Column
    private Float region_latitude; // 위도

    @Column
    private Float region_longitude; // 경도

    @Column
    private Boolean has_room;

    @Column
    private Boolean pets_allowed;

    @Column
    private Float distance;

    @Column
    private Timestamp createdAt; // 이 타입 맞나 확인

    @Column
    private Boolean snoring;

    @Column
    private Boolean smoking;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifecycle lifeStyle;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToOne
    private ChattingRoom chattingRoom;

    @ManyToOne
    private ApplyRecord applyRecords;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;



}
