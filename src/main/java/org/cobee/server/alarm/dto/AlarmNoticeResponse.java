package org.cobee.server.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.cobee.server.alarm.domain.Alarm;
import org.cobee.server.alarm.domain.AlarmNotice;
@Builder
@Getter
@AllArgsConstructor
public class AlarmNoticeResponse {

        private Long noticeId;
        private boolean isRead;
        private Long alarmId;
        private String alarmType;
        private String sourceType;
        private Long sourceId;
        private Long fromUserId;
        private Long toUserId;

        public static AlarmNoticeResponse of(AlarmNotice notice) {
            Alarm alarm = notice.getAlarm();
            return AlarmNoticeResponse.builder()
                    .noticeId(notice.getId())
                    .isRead(notice.getIsRead())
                    .alarmId(alarm.getId())
                    .alarmType(alarm.getAlarmType().name())
                    .sourceType(alarm.getSourceType().name())
                    .sourceId(alarm.getSourceId())
                    .fromUserId(alarm.getMember().getId())
                    .toUserId(notice.getMember().getId())
                    .build();
        }

}
