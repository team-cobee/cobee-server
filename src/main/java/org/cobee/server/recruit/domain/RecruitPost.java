package org.cobee.server.recruit.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;
import org.cobee.server.publicProfile.domain.enums.Pets;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import org.cobee.server.chat.domain.ChatRoom;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.dto.RecruitRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column
    private int recruitCountMin;

    @Column
    private int rentCostMin;

    @Column
    private int rentCostMax;

    @Column
    private int monthlyCostMin;

    @Column
    private int monthlyCostMax;

    @Column
    private int minAge;

    @Column
    private int maxAge;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifestyle lifeStyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    private Boolean isSmoking;

    @Column
    private Boolean isSnoring;

    @Column
    private Pets isPetsAllowed;

    @Column
    private Boolean hasRoom;

    @Column
    private String address;

    @Column
    @Lob
    private String detailDescription;

    @Column
    @Lob
    private String additionalDescription;

    @Column
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Column
    private LocalDateTime createdAt;


    /* 지도 */

    @Column
    private Float regionLatitude; // 위도

    @Column
    private Float regionLongitude; // 경도

    @Column
    private Float distance;  // 거리

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToOne
    private ChatRoom chattingRoom;

    @OneToMany(mappedBy = "post")
    private List<ApplyRecord> applyRecords = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="user_id")
    private Member member;


    public void updatePost(RecruitRequest dto) {
        this.title = dto.getTitle();
        this.recruitCount = dto.getRecruitCount();
        this.rentCost = dto.getRentCost();
        this.monthlyCost = dto.getMonthlyCost();
        //this.imgUrl = dto.imgUrl();
        this.content = dto.getContent();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addApply(ApplyRecord apply) {
        this.applyRecords.add(apply);
        apply.setPost(this); // FK 가진 소유자 쪽도 세팅
    }

}