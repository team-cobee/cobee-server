package org.cobee.server.alarm.repository;

import org.cobee.server.alarm.domain.AlarmNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmNoticeRepository extends JpaRepository<AlarmNotice, Long> {
    AlarmNotice findAlarmNoticeByAlarmId(Long id);
    @Query("select notices from AlarmNotice notices where notices.member.id=:toUserId")
    List<AlarmNotice> findMyAllAlarmNotice(@Param("toUserId") Long toUserId);
}

