package org.cobee.server.recruit.domain;

import jakarta.persistence.*;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.global.BaseEntity;
import org.cobee.server.image.domain.Images;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.*;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.member.domain.Member;
import org.cobee.server.recruit.dto.RecruitRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecruitPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private Integer recruitCount;

    @Column
    private Integer rentCostMin;

    @Column
    private Integer rentCostMax;

    @Column
    private Integer monthlyCostMin;

    @Column
    private Integer monthlyCostMax;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender preferedGender;

    @Column
    private Integer minAge;

    @Column
    private Integer maxAge;

    @Column
    @Enumerated(EnumType.STRING)
    private Lifestyle lifeStyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    @Enumerated(EnumType.STRING)
    private Smoking isSmoking;

    @Column
    @Enumerated(EnumType.STRING)
    private Snoring isSnoring;

    @Column
    @Enumerated(EnumType.STRING)
    private Pets isPetsAllowed;

    @Column
    private Boolean hasRoom;

    @Column
    private String address;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Column(columnDefinition = "TEXT")
    private String additionalDescription;

    @Column
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Column
    private Double regionLatitude; // 위도

    @Column
    private Double regionLongitude; // 경도

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<ApplyRecord> applyRecords = new ArrayList<>();

    @OneToMany(mappedBy = "recruitPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Images> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;


    public void updatePost(RecruitRequest dto, Map<String, Object> geocodeData) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getRecruitCount() != null) {
            this.recruitCount = dto.getRecruitCount();
        }
        if (dto.getRentCostMin() != null) {
            this.rentCostMin = dto.getRentCostMin();
        }
        if (dto.getRentCostMax() != null) {
            this.rentCostMax = dto.getRentCostMax();
        }
        if (dto.getMonthlyCostMin() != null) {
            this.monthlyCostMin = dto.getMonthlyCostMin();
        }
        if (dto.getMonthlyCostMax() != null) {
            this.monthlyCostMax = dto.getMonthlyCostMax();
        }
        if (dto.getMinAge() != null) {
            this.minAge = dto.getMinAge();
        }
        if (dto.getMaxAge() != null) {
            this.maxAge = dto.getMaxAge();
        }
        if (dto.getLifestyle() != null) {
            this.lifeStyle = dto.getLifestyle();
        }
        if (dto.getPersonality() != null) {
            this.personality = dto.getPersonality();
        }
        if (dto.getIsSmoking() != null) {
            this.isSmoking = dto.getIsSmoking();
        }
        if (dto.getIsSnoring() != null) {
            this.isSnoring = dto.getIsSnoring();
        }
        if (dto.getIsPetsAllowed() != null) {
            this.isPetsAllowed = dto.getIsPetsAllowed();
        }
        if (dto.getHasRoom() != null) {
            this.hasRoom = dto.getHasRoom();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }

        if (geocodeData != null) {
            this.address = (String) geocodeData.get("formattedAddress");
            this.regionLatitude = (Double) geocodeData.get("latitude");
            this.regionLongitude = (Double) geocodeData.get("longitude");
        }

    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void addApply(ApplyRecord apply) {
        this.applyRecords.add(apply);
        apply.setPost(this); // FK 가진 소유자 쪽도 세팅
    }

    public void updateStatus(RecruitStatus status) {
        this.status = status;
    }

}