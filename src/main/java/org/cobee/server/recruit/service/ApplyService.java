package org.cobee.server.recruit.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;
import org.cobee.server.alarm.dto.AlarmCreateRequest;
import org.cobee.server.alarm.service.AlarmService;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.domain.enums.MatchStatus;
import org.cobee.server.recruit.dto.*;
import org.cobee.server.recruit.repository.ApplyRecordRepository;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplyService {
    private final MemberRepository memberRepository;
    private final RecruitPostRepository postRepository;
    private final ApplyRecordRepository applyRepository;
    private final AlarmService alarmService;
    private final ApplicationEventPublisher publisher;

    // TODO : 중복 처리 되지 않게 수정
    @Transactional
    public ApplyResponse apply(Long memberId, ApplyRequest request) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        RecruitPost post = postRepository.findById(request.getPostId()).orElseThrow();
        ApplyRecord applyRecord = ApplyRecord.builder()
                .post(post)
                .member(member)
                .isMatched(MatchStatus.ON_WAIT)
                .submittedAt(LocalDate.now())
                .build();

        post.addApply(applyRecord);
        //postRepository.save(post); 이거 대신에 RecruitPost에 setting하는 메서드로
        applyRepository.save(applyRecord);

        // 알림 생성 및 발송
        publisher.publishEvent(new ApplyCreatedEvent(
                applyRecord.getId(),
                post.getId(),
                memberId,
                post.getMember().getId()
        ));

        return ApplyResponse.from(applyRecord);
    }

    @Transactional
    public ApplyResponse accept(Long memberId, Long applyId, ApplyAcceptRequest applyAccept){
        ApplyRecord applyRecord = applyRepository.findById(applyId).orElseThrow(()->new CustomException(ErrorCode.APPLY_NOT_FOUND));
        Long checkAuthor = applyRecord.getPost().getMember().getId();
        if(memberId.equals(checkAuthor)) {
            Boolean accept = applyAccept.getIsAccept();
            applyRecord.acceptMatching(accept);
            applyRepository.save(applyRecord);

            publisher.publishEvent(new ApplyAcceptResultEvent(
                    applyRecord.getId(),
                    applyRecord.getPost().getId(),
                    memberId,
                    applyRecord.getMember().getId(),
                    accept
            ));

            return ApplyResponse.from(applyRecord);
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

    }

    public List<RecruitResponse> getMyAppliesOnWait(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<ApplyRecord> myRecords = applyRepository.findApplyRecordsByMemberIdAndStatus(memberId, MatchStatus.ON_WAIT);
        List<RecruitResponse> myApplies = new ArrayList<>();
        for (ApplyRecord record : myRecords){
            myApplies.add(RecruitResponse.from(record.getPost(), member));
        }
        return myApplies;
    }

    public List<RecruitResponse> getMyAppliesOnMatching(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<ApplyRecord> myRecords = applyRepository.findApplyRecordsByMemberIdAndStatus(memberId, MatchStatus.MATCHING);
        List<RecruitResponse> myApplies = new ArrayList<>();
        for (ApplyRecord record : myRecords){
            myApplies.add(RecruitResponse.from(record.getPost(), member));
        }
        return myApplies;
    }

    public List<RecruitResponse> getMyAppliesOnMatched(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        List<ApplyRecord> myRecords = applyRepository.findApplyRecordsByMemberIdAndStatus(memberId, MatchStatus.MATCHED);
        List<RecruitResponse> myApplies = new ArrayList<>();
        for (ApplyRecord record : myRecords){
            myApplies.add(RecruitResponse.from(record.getPost(), member));
        }
        return myApplies;
    }

    public List<PublicProfileResponseDto> getMyAllPostAppliers(Long postId, Long memberId) {
        // 작성자 본인 확인 체크
        RecruitPost post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long checkAuthor = post.getMember().getId();

        if (checkAuthor.equals(memberId)){
            List<ApplyRecord> records = applyRepository.findMyPostAppliers(postId);
            List<PublicProfileResponseDto> appliers = new ArrayList<>();

            for (ApplyRecord record : records){
                appliers.add(PublicProfileResponseDto.from(record.getMember().getPublicProfile(), record.getMember()));
            }
            return appliers;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

    }

    public List<PublicProfileResponseDto> getMyOnConsideredPostAppliers(Long postId, Long memberId) {
        // 작성자 본인 확인 체크
        RecruitPost post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long checkAuthor = post.getMember().getId();


        if (checkAuthor.equals(memberId)){
            List<ApplyRecord> records = applyRepository.findApplyProfilesByMemberIdAndStatus(postId, MatchStatus.ON_WAIT);
            List<PublicProfileResponseDto> appliers = new ArrayList<>();
            for (ApplyRecord record : records){
                appliers.add(PublicProfileResponseDto.from(record.getMember().getPublicProfile(), record.getMember()));
            }
            return appliers;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public List<PublicProfileResponseDto> getMyOnApprovedPostAppliers(Long postId, Long memberId) {
        // 작성자 본인 확인 체크
        RecruitPost post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long checkAuthor = post.getMember().getId();

        if (checkAuthor.equals(memberId)){
            List<ApplyRecord> records = applyRepository.findApplyProfilesByMemberIdAndStatus(postId, MatchStatus.MATCHING);
            List<PublicProfileResponseDto> appliers = new ArrayList<>();

            for (ApplyRecord record : records){
                appliers.add(PublicProfileResponseDto.from(record.getMember().getPublicProfile(), record.getMember()));
            }
            return appliers;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public List<PublicProfileResponseDto> getMyRejectedPostAppliers(Long postId, Long memberId) {
        // 작성자 본인 확인 체크
        RecruitPost post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        Long checkAuthor = post.getMember().getId();

        if (checkAuthor.equals(memberId)){
            List<ApplyRecord> records = applyRepository.findApplyProfilesByMemberIdAndStatus(postId, MatchStatus.REJECTED);
            List<PublicProfileResponseDto> appliers = new ArrayList<>();

            for (ApplyRecord record : records){
                appliers.add(PublicProfileResponseDto.from(record.getMember().getPublicProfile(), record.getMember()));
            }
            return appliers;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }


}
