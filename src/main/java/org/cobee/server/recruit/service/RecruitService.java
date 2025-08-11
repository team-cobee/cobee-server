package org.cobee.server.recruit.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.RecruitStatus;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecruitService {
    private final RecruitPostRepository recruitRepository;
    private final MemberRepository memberRepository;

    public RecruitResponse createRecruitPost(RecruitRequest request, Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        RecruitPost recruitPost = RecruitPost.builder()
                .title(request.title())
                .content(request.content())
                .recruitCount(request.recruitCount())
                .status(RecruitStatus.RECRUITING)
                .rentCost(request.rentCost())
                .monthlyCost(request.monthlyCost())
                //.regionLatitude
                //.regionLongitude
                .hasRoom(true)
                //.isPetsAllowed()
                //.distance()
                .createdAt(LocalDateTime.now())
                //.isSnoring()
                //.isSmoking()
                //.personality()
                //.lifeStyle()
                .member(member)
                .build();
        recruitRepository.save(recruitPost);


        return RecruitResponse.from(recruitPost, member);
    }

    public RecruitResponse updateRecruitPost(RecruitRequest request, Long postId, Long memberId){
        RecruitPost post = recruitRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
        post.updatePost(request);
        recruitRepository.save(post);
        return RecruitResponse.from(post, member);
    }

    public RecruitResponse getRecruitPost(Long postId) {
        RecruitPost post = recruitRepository.findById(postId).orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
        Member member = post.getMember();
        return RecruitResponse.from(post, member);
    }

    public List<RecruitResponse> getAllRecruitPosts() {
        List<RecruitPost> posts = recruitRepository.findAll();
        List<RecruitResponse> result = new ArrayList<>();
        for (RecruitPost post : posts){
            result.add(RecruitResponse.from(post, post.getMember()));
        }
        return result;
    }

    public Boolean deleteRecruitPost(Long postId) {
        try{
            RecruitPost post = recruitRepository.findById(postId).orElseThrow(()->new CustomException(ErrorCode.POST_NOT_FOUND));
            recruitRepository.delete(post);
            return true;
        } catch (CustomException e) {
            return false;
        }


    }
}
