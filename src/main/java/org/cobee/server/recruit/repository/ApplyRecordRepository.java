package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.domain.ApplyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRecordRepository extends JpaRepository<ApplyRecord, Long> {
    List<ApplyRecord> findApplyRecordsByMemberId(Long memberId);
}
