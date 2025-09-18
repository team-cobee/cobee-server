package org.cobee.server.recruit.service;

import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.image.domain.Images;
import org.cobee.server.image.repository.ImagesRepository;
import org.cobee.server.image.service.DataBucketUtil;
import org.cobee.server.image.service.ImageValidationUtil;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitPostRepository recruitRepository;
    private final MemberRepository memberRepository;
    private final ApplyRecordRepository applyRepository;
    private final GoogleMapService googleMapService;
    private final DataBucketUtil dataBucketUtil;
    private final ImagesRepository imagesRepository;

    @Transactional
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

    @Transactional
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
    @Transactional(readOnly = true)
    public RecruitResponse getRecruitPost(Long postId) {
        RecruitPost post = recruitRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = post.getMember();
        return RecruitResponse.from(post, member);
    }
    @Transactional(readOnly = true)
    public List<RecruitResponse> getAllRecruitPosts() {
        List<RecruitPost> posts = recruitRepository.findAll();
        List<RecruitResponse> result = new ArrayList<>();
        for (RecruitPost post : posts) {
            result.add(RecruitResponse.from(post, post.getMember()));
        }
        return result;
    }
    @Transactional
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

    public List<String> addImages(MultipartFile[] files, Long postId, Long memberId) {
        // 권한 확인 (본인 게시글인지)
        RecruitPost post = recruitRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        // 파일 검증
        ImageValidationUtil.validateMultipleImageFiles(files);
        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            try {
                String imageUrl = dataBucketUtil.uploadImage(files[i]);
                // Images 엔티티에 저장
                Images image = Images.builder()
                        .imageUrl(imageUrl)
                        .originalName(files[i].getOriginalFilename())
                        .displayOrder(i + 1)
                        .recruitPost(post)
                        .build();
                imagesRepository.save(image);

                imageUrls.add(imageUrl);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        return imageUrls;
    }
    @Transactional
    public RecruitResponse updateRecruitStatus(Long memberId, Long postId, RecruitStatus status) {
        Member member  = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        RecruitPost post = recruitRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        post.updateStatus(status);
        recruitRepository.save(post);
        return RecruitResponse.from(post, member);
    }
}
