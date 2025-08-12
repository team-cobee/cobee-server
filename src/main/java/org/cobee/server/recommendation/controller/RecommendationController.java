package org.cobee.server.recommendation.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recommendation.service.MLRecommendationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendation", description = "추천 시스템 API")
public class RecommendationController {
    
    private final MLRecommendationService mlRecommendationService;
    
    @GetMapping("/{userId}")
    @Operation(summary = "사용자 추천 결과 조회", description = "특정 사용자에 대한 룸메이트 추천 결과를 조회합니다.")
    public ApiResponse<?> getRecommendations(@PathVariable Long userId, 
                                           @RequestParam(defaultValue = "5") int topN) {
        Object recommendations = mlRecommendationService.getRecommendations(userId);
        if (recommendations == null) {
            return ApiResponse.error("추천 결과를 조회할 수 없습니다.");
        }
        return ApiResponse.success(recommendations);
    }
    
    @GetMapping("/batch/status")
    @Operation(summary = "배치 처리 상태 조회", description = "ML 배치 처리 상태를 조회합니다.")
    public ApiResponse<?> getBatchStatus() {
        Object status = mlRecommendationService.getBatchStatus();
        if (status == null) {
            return ApiResponse.error("배치 상태를 조회할 수 없습니다.");
        }
        return ApiResponse.success(status);
    }
    
    @GetMapping("/batch/result")
    @Operation(summary = "배치 처리 결과 조회", description = "ML 배치 처리 결과를 조회합니다.")
    public ApiResponse<?> getBatchResult() {
        Object result = mlRecommendationService.getBatchResult();
        if (result == null) {
            return ApiResponse.error("배치 결과를 조회할 수 없습니다.");
        }
        return ApiResponse.success(result);
    }
    
    @PostMapping("/batch/sync")
    @Operation(summary = "수동 데이터 동기화", description = "ML API로 수동으로 데이터를 동기화합니다.")
    public ApiResponse<?> manualSync() {
        try {
            mlRecommendationService.syncDataToML();
            return ApiResponse.success("데이터 동기화가 시작되었습니다.");
        } catch (Exception e) {
            return ApiResponse.error("데이터 동기화 실패: " + e.getMessage());
        }
    }
}