package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.domain.ApplyRecord;
import org.cobee.server.recruit.domain.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRecordRepository extends JpaRepository<ApplyRecord, Long> {

    // 내가 쓴 구인글에 지원한 사람들의 기록 (내 구인글 탭에서 지원자 승인 받는 곳)
    @Query("select applies from ApplyRecord applies where applies.post.id=:postId and applies.member.id=:memberId")
    List<ApplyRecord> findMyPostAppliers(@Param("memberId") Long memberId, @Param("postId") Long postId);

    // 내가 지원한 구인글(= ON_WAIT) 가져오기(프론트상 첫번째 탭), MATCHING인 것 status 변수로 가져오기
    @Query("select applies from ApplyRecord applies where applies.isMatched=:status and applies.member.id=:memberId")
    List<ApplyRecord> findApplyRecordsByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") MatchStatus status);

    // 매칭 완료된 구인글 보기 (MATCHED 상태 - 프론트상 2번쨰 탭)



}
