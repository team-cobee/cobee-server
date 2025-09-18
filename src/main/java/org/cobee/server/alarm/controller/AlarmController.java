package org.cobee.server.alarm.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.alarm.dto.AlarmNoticeResponse;
import org.cobee.server.alarm.dto.MarkReadRequest;
import org.cobee.server.alarm.service.AlarmService;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/alarm")
public class AlarmController {
    private final AlarmService alarmService;
    @PostMapping("/read")
    public ApiResponse<Boolean> readAlarm(@RequestBody MarkReadRequest request){
        Boolean result = alarmService.changeAlarmRead(request);
        return ApiResponse.success("", "", result);
    }

    @GetMapping("/my")
    public ApiResponse<List<AlarmNoticeResponse>> myAlarm(){

    }
}