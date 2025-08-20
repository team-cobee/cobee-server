package org.cobee.server.alarm.repository;

import org.cobee.server.alarm.domain.AlarmNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmNoticeRepository extends JpaRepository<AlarmNotice, Long> {
    AlarmNotice findAlarmNoticeByAlarmId(Long id);
}

