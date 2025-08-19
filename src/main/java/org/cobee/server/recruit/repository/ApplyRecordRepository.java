package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.domain.ApplyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRecordRepository extends JpaRepository<ApplyRecord, Long> {
    List<ApplyRecord> findApplyRecordsByMemberId(Long memberId);
    @Query("select applies from ApplyRecord applies where applies.post.id=:postId")
    List<ApplyRecord> findMyPostAppliers(@Param("postId") Long postId);
}
