package org.cobee.server.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.cobee.server.alarm.dto.AlarmCreateRequest;
import org.cobee.server.alarm.dto.AlarmNoticeResponse;
import org.cobee.server.alarm.service.AlarmService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/alarms")
public class AlarmController {

    private final AlarmService alarmService;
    @PostMapping
    public AlarmNoticeResponse create(@RequestBody AlarmCreateRequest req) {
        return alarmService.createAndSend(req);
    }
}
