package org.cobee.server.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.cobee.server.alarm.domain.enums.AlarmSourceType;
import org.cobee.server.alarm.domain.enums.AlarmType;
@Getter
@AllArgsConstructor
public class AlarmCreateRequest{
    private Long fromUserId;
    private Long toUserId;
    private AlarmType alarmType;
    private AlarmSourceType sourceType;
    private Long sourceId;
    private String pushTitle;   // 푸시 타이틀
    private String pushBody;    // 푸시 내용

}

