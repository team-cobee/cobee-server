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

    // 내가 쓴 구인글에 지원한 사람들의 모든 공개프로필 리스트 (조건상관X, 내 구인글 탭에서 지원자 승인 받는 곳)
    @Query("select applies from ApplyRecord applies where applies.post.id=:postId")
    List<ApplyRecord> findMyPostAppliers(@Param("postId") Long postId);

    // 내가 지원한 구인글 매칭상태 변수에 따라 가져오기(on wait, matching, matched)
    @Query("select applies from ApplyRecord applies where applies.isMatched=:status and applies.member.id=:memberId")
    List<ApplyRecord> findApplyRecordsByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") MatchStatus status);

    @Query("select applies from ApplyRecord applies where applies.isMatched=:status and applies.post.id=:postId")
    List<ApplyRecord> findApplyProfilesByMemberIdAndStatus(@Param("postId") Long postId, @Param("status") MatchStatus status);


}
