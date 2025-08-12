package org.cobee.server.member.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.member.dto.UserPreferencesRequestDto;
import org.cobee.server.member.dto.UserPreferencesResponseDto;
import org.cobee.server.member.service.UserPreferencesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class UserPreferencesController {

    private final UserPreferencesService userPreferencesService;

    /**
     * 사용자 선호도 등록
     */
    @PostMapping("/{memberId}")
    public ApiResponse<UserPreferencesResponseDto> createUserPreferences(
            @PathVariable Long memberId,
            @RequestBody UserPreferencesRequestDto requestDto) {

        UserPreferencesResponseDto responseDto = userPreferencesService.createUserPreferences(memberId, requestDto);

        return ApiResponse.success("사용자 선호도 등록이 완료되었습니다.", "201", responseDto);
    }

    /**
     * 사용자 선호도 조회
     */
    @GetMapping("/{memberId}")
    public ApiResponse<UserPreferencesResponseDto> getUserPreferences(
            @PathVariable Long memberId) {

        UserPreferencesResponseDto responseDto = userPreferencesService.getUserPreferences(memberId);

        return ApiResponse.success("사용자 선호도 조회가 완료되었습니다.", "200", responseDto);
    }

    /**
     * 사용자 선호도 수정
     */
    @PutMapping("/{memberId}")
    public ApiResponse<UserPreferencesResponseDto> updateUserPreferences(
            @PathVariable Long memberId,
            @RequestBody UserPreferencesRequestDto requestDto) {

        UserPreferencesResponseDto responseDto = userPreferencesService.updateUserPreferences(memberId, requestDto);

        return ApiResponse.success("사용자 선호도 수정이 완료되었습니다.", "200", responseDto);
    }
}