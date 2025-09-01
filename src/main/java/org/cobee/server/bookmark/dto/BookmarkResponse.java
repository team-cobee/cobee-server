package org.cobee.server.bookmark.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.bookmark.domain.Bookmark;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.publicProfile.domain.enums.*;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import java.time.LocalDateTime;
@Getter
@Builder
public class BookmarkResponse {
    /* 제목 */
    private Long postId;
    private String title;
    private RecruitStatus status;
    /* 작성자 정보 */
    private String authorName;

    /* 구인글 정보 */
    private Integer recruitCount;
    private Integer rentalCostMin;
    private Integer rentalCostMax;
    private Integer monthlyCostMin;
    private Integer monthlyCostMax;

    /* 구인글 메이트 선호 정보 */
    private Gender preferedGender;
    private Integer preferedMinAge;
    private Integer preferedMaxAge;
    private Lifestyle preferedLifeStyle;
    private Personality preferedPersonality;
    private Smoking preferedSmoking;
    private Snoring preferedSnoring;
    private Pets preferedHasPet;
    /* 주소 정보 */
    private String address;
    /*bookmark 생성일*/
    private LocalDateTime createdAt;

    public static BookmarkResponse from(RecruitPost post, Member member, Bookmark bookmark) {

        return BookmarkResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .status(post.getStatus())
                .authorName(post.getMember().getName())
                .recruitCount(post.getRecruitCount())
                .rentalCostMin(post.getRentCostMin())
                .rentalCostMax(post.getRentCostMax())
                .monthlyCostMin(post.getMonthlyCostMin())
                .monthlyCostMax(post.getMonthlyCostMax())

                .preferedGender(post.getPreferedGender())
                .preferedMinAge(post.getMinAge())
                .preferedMaxAge(post.getMaxAge())
                .preferedLifeStyle(post.getLifeStyle())
                .preferedPersonality(post.getPersonality())
                .preferedSmoking(post.getIsSmoking())
                .preferedSnoring(post.getIsSnoring())
                .preferedHasPet(post.getIsPetsAllowed())
                .address(post.getAddress())
                .createdAt(bookmark.getCreatedAt())
                .build();
    }

}
