package org.cobee.server.recruit.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.map.service.GoogleMapService;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.MatchStatus;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.repository.ApplyRecordRepository;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitPostRepository recruitRepository;
    private final MemberRepository memberRepository;
    private final ApplyRecordRepository applyRepository;
    private final GoogleMapService googleMapService;

    public RecruitResponse createRecruitPost(RecruitRequest request, Long memberId) {
        Map<String, Object> geocodeData = googleMapService.getGeocode(request.getAddress());
        double latitude = (double) geocodeData.get("latitude");
        double longitude = (double) geocodeData.get("longitude");
        String formattedAddress = (String) geocodeData.get("formattedAddress");
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        RecruitPost recruitPost = RecruitPost.builder()
                .title(request.getTitle())
                .recruitCount(request.getRecruitCount())
                .rentCostMin(request.getRentCostMin())
                .rentCostMax(request.getRentCostMax())
                .monthlyCostMin(request.getMonthlyCostMin())
                .monthlyCostMax(request.getMonthlyCostMax())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .lifeStyle(request.getLifestyle())
                .personality(request.getPersonality())
                .isSmoking(request.getIsSmoking())
                .isSnoring(request.getIsSnoring())
                .isPetsAllowed(request.getIsPetsAllowed())
                .hasRoom(request.getHasRoom())
                .address(formattedAddress)
                .createdAt(LocalDateTime.now())
                .status(RecruitStatus.RECRUITING)
                .regionLongitude(longitude)
                .regionLatitude(latitude)
                .detailDescription(request.getDetailDescription())
                .additionalDescription(request.getAdditionalDescription())
                .comments(new ArrayList<>()) //여기에 추가하는건 안되고 메서드로 추가해야하는건가??
                .member(member)

                .build();
        recruitRepository.save(recruitPost);

        ApplyRecord apply = ApplyRecord.builder()
                .submittedAt(LocalDate.now())
                .post(recruitPost)
                .isMatched(MatchStatus.MATCHING)
                .member(member)
                .build();
        applyRepository.save(apply);

        return RecruitResponse.from(recruitPost, member);
    }

    public RecruitResponse updateRecruitPost(RecruitRequest request, Long postId, Long memberId) {
        RecruitPost post = recruitRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Map<String, Object> geocodeData = null;
        if (request.getAddress() != null && !request.getAddress().equals(post.getAddress())) {
            geocodeData = googleMapService.getGeocode(request.getAddress());
        }

        post.updatePost(request, geocodeData);
        recruitRepository.save(post);
        return RecruitResponse.from(post, member);
    }

    public RecruitResponse getRecruitPost(Long postId) {
        RecruitPost post = recruitRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = post.getMember();
        return RecruitResponse.from(post, member);
    }

    public List<RecruitResponse> getAllRecruitPosts() {
        List<RecruitPost> posts = recruitRepository.findAll();
        List<RecruitResponse> result = new ArrayList<>();
        for (RecruitPost post : posts) {
            result.add(RecruitResponse.from(post, post.getMember()));
        }
        return result;
    }

    public Boolean deleteRecruitPost(Long postId) {
        try {
            RecruitPost post = recruitRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
            recruitRepository.delete(post);
            return true;
        } catch (CustomException e) {
            return false;
        }

    }

    @Transactional(readOnly = true)
    public List<RecruitResponse> getAllMyPost(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<RecruitPost> myPosts = recruitRepository.findAllByMemberId(memberId);
        List<RecruitResponse> responses = new ArrayList<>();
        for (RecruitPost post : myPosts) {
            responses.add(RecruitResponse.from(post, member));
        }
        return responses;
    }

    @Transactional(readOnly = true)
    public List<RecruitResponse> getfilterRecruitPosts(Double latitude, Double longitude, Double radius,
                                                       Integer recruitCount, Integer rentCostMin, Integer rentCostMax,
                                                       Integer monthlyCostMin, Integer monthlyCostMax) {
        List<RecruitPost> posts;

        if (latitude != null && longitude != null && radius != null) {
            posts = recruitRepository.findFilteredRecruitPosts(latitude, longitude, radius, recruitCount, rentCostMin,
                    rentCostMax, monthlyCostMin, monthlyCostMax);
        } else {
            posts = recruitRepository.findFilteredRecruitPosts(null, null, null, recruitCount, rentCostMin, rentCostMax,
                    monthlyCostMin, monthlyCostMax);
        }

        List<RecruitResponse> result = new ArrayList<>();
        for (RecruitPost post : posts) {
            result.add(RecruitResponse.from(post, post.getMember()));
        }
        return result;
    }
}
