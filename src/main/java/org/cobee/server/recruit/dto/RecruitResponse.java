package org.cobee.server.recruit.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.comment.domain.Comment;
import org.cobee.server.comment.dto.CommentResponse;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.domain.enums.Gender;
import org.cobee.server.member.domain.enums.SocialType;
import org.cobee.server.publicProfile.domain.enums.*;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.RecruitStatus;

import java.util.ArrayList;
import java.util.List;

/*
TODO
- response에 댓글개수, 조회수(가능하면), 지원자 n명 추가
 */
@Builder
@Getter
public class RecruitResponse{
    /* 제목 */
    private Long postId;
    private String title;
    private String address;
    private Integer viewed;
    private Integer bookmarked;
    private String createdAt;
    private RecruitStatus status;

    /* 작성자 정보 */
    private String authorName;
    private Gender authorGender;
    private String birthdate; // 나이 변환은 프론트에서??

    /* 구인글 정보 */
    private Integer recruitCount;
    private Boolean hasRoom;
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

    /* 지도 정보 */
    private Float latitude;
    private Float longitude;
    private Float distance;

    /* 추가 정보 */
    private String detailDescript;
    private String additionalDescript;

    /* 나중에 추가해야하는 것
    * private String firstImage;
    * private String authorProfileImg;
    */

    private List<CommentResponse> comments;

    public static RecruitResponse from(RecruitPost post, Member member) {
        List<CommentResponse> responses = new ArrayList<>();
        List<Comment> result = post.getComments();
        if (result != null) {
            for (Comment comment : result) {
                if (comment != null) {
                    responses.add(CommentResponse.from(member, comment));
                }
            }
        }


        return RecruitResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .viewed(0)
                .bookmarked(0)
                .status(post.getStatus())
                .createdAt(post.getCreatedAt().toString())

                .authorName(post.getMember().getName())
                .authorGender(Gender.valueOf(post.getMember().getGender()))
                .birthdate(post.getMember().getBirthDate())

                .recruitCount(post.getRecruitCount())
                .hasRoom(post.getHasRoom())
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

                .latitude(post.getRegionLatitude())
                .longitude(post.getRegionLongitude())
                .distance(post.getDistance())

                .detailDescript(post.getDetailDescription())
                .additionalDescript(post.getAdditionalDescription())

                .comments(responses)
                .build();
    }
}

