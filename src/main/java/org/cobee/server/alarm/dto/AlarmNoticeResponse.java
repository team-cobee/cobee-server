package org.cobee.server.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.alarm.domain.AlarmNotice;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;

@Builder
@Getter
@AllArgsConstructor
public class AlarmNoticeResponse {

        private Long noticeId;
        private boolean isRead;
        private Long alarmId;
        private AlarmType alarmType;
        private AlarmSourceType sourceType;
        private Long sourceId;
        private Long fromUserId;
        private Long toUserId;

        public static AlarmNoticeResponse of(AlarmNotice notice) {
            Alarm alarm = notice.getAlarm();
            return AlarmNoticeResponse.builder()
                    .noticeId(notice.getId())
                    .isRead(notice.getIsRead())
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType())
                    .sourceType(alarm.getSourceType())
                    .sourceId(alarm.getSourceId())
                    .fromUserId(alarm.getMember().getId())
                    .toUserId(notice.getMember().getId())
                    .build();
        }

}
