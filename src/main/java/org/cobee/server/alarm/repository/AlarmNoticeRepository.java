package org.cobee.server.alarm.repository;

import org.cobee.server.alarm.domain.AlarmNotice;
import org.cobee.server.member.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmNoticeRepository extends JpaRepository<AlarmNotice, Long> {
    Page<AlarmNotice> findByMemberOrderByIdDesc(Member toUser, Pageable pageable);
    long countByMemberAndIsReadFalse(Member toUser);
}