package org.cobee.server.recruit.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.MatchStatus;
import org.cobee.server.recruit.dto.ApplyAcceptRequest;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.cobee.server.recruit.repository.ApplyRecordRepository;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

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
        ApplyRecord applyRecord = applyRepository.findById(applyId).orElseThrow();
        Boolean accept = applyAccept.getIsAccept();
        applyRecord.acceptMatching(accept);
        applyRepository.save(applyRecord);
        return ApplyResponse.from(applyRecord);
    }

}
