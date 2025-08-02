package org.cobee.server.recruit.repository;

import org.cobee.server.recruit.entity.ApplyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplyRecordRepository extends JpaRepository<ApplyRecord, Long> {
}
