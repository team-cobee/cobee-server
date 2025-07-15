package org.cobee.server.recruit;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.Enum.Lifecycle;
import org.cobee.server.Enum.Personality;
import org.cobee.server.Enum.RecruitStatus;
import org.cobee.server.chat.ChattingRoom;
import org.cobee.server.comment.Comment;
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
    private int recruitCount;

    @Column
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Column
    private int rentCost;

    @Column
    private Float regionLatitude; // 위도

    @Column
    private Float regionLongitude; // 경도

    @Column
    private Boolean hasRoom;

    @Column
    private Boolean petsAllowed;

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
