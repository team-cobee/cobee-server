package org.cobee.server.recruit.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.MatchStatus;
import org.cobee.server.recruit.dto.ApplyAcceptRequest;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.repository.ApplyRecordRepository;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final MemberRepository memberRepository;
    private final RecruitPostRepository postRepository;
    private final ApplyRecordRepository applyRepository;
    public ApplyResponse apply(Long memberId, ApplyRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        RecruitPost post = postRepository.findById(request.getPostId()).orElseThrow();
        ApplyRecord applyRecord = ApplyRecord.builder()
                .post(post)
                .member(member)
                .isMatched(MatchStatus.NONE)
                .submittedAt(LocalDate.now())
                .build();

        post.addApply(applyRecord);
        //postRepository.save(post); 이거 대신에 RecruitPost에 setting하는 메서드로
        applyRepository.save(applyRecord);
        return ApplyResponse.from(applyRecord);
    }

    public ApplyResponse accept(Long applyId, ApplyAcceptRequest applyAccept){
        ApplyRecord applyRecord = applyRepository.findById(applyId).orElseThrow(()->new CustomException(ErrorCode.APPLY_NOT_FOUND));
        Boolean accept = applyAccept.getIsAccept();
        applyRecord.acceptMatching(accept);
        applyRepository.save(applyRecord);
        return ApplyResponse.from(applyRecord);
    }

    public List<RecruitResponse> getMyApplies(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<ApplyRecord> myRecords = applyRepository.findApplyRecordsByMemberId(memberId);
        List<RecruitResponse> myApplies = new ArrayList<>();
        for (ApplyRecord record : myRecords){
            myApplies.add(RecruitResponse.from(record.getPost(), member));
        }
        return myApplies;
    }

    public List<PublicProfileResponseDto> getMyAppliers(Long postId, Long memberId) {
        // TODO : 현재 로그인된 사용자 검증은 하는데 작성자인지 검증하는거 넣어야함.
        memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        List<ApplyRecord> records = applyRepository.findMyPostAppliers(postId);
        List<PublicProfileResponseDto> appliers = new ArrayList<>();
        // get()은 null이 있다면 NPE 터뜨림 -> TODO : 근데 현재 공개프로필은 필수 작성이므로 NPE 처리 안 함 (나중에 필요시 리팩토링할것)
        for (ApplyRecord record : records){
            appliers.add(PublicProfileResponseDto.from(record.getMember().getPublicProfile(), record.getMember()));
        }
        return appliers;
    }
}
