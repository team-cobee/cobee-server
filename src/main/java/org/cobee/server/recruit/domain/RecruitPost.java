package org.cobee.server.recruit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import org.cobee.server.chat.domain.ChattingRoom;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.dto.RecruitRequest;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
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
    private int monthlyCost;

    @Column
    private Float regionLatitude; // 위도

    @Column
    private Float regionLongitude; // 경도

    @Column
    private Boolean hasRoom;

    @Column
    private Boolean isPetsAllowed;

    @Column
    private Float distance;

    @Column
    private LocalDateTime createdAt;

    @Column
    private Boolean isSnoring;

    @Column
    private Boolean isSmoking;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifestyle lifeStyle;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToOne
    private ChattingRoom chattingRoom;

    @ManyToOne
    private ApplyRecord applyRecords;

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;


    public void updatePost(RecruitRequest dto) {
        this.title = dto.title();
        this.recruitCount = dto.recruitCount();
        this.rentCost = dto.rentCost();
        this.monthlyCost = dto.monthlyCost();
        //this.imgUrl = dto.imgUrl();
        this.content = dto.content();
    }
}
