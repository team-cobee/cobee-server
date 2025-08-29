package org.cobee.server.recruit.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.RecruitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MapController {
    private final RecruitService recruitService;

    //구인글 필터 (위치, 모집인원, 월세, 보증금)
    @GetMapping("/posts/filter")
    public ApiResponse<List<RecruitResponse>> getRecruitPosts(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Integer recruitCount,
            @RequestParam(required = false) Integer rentCostMin,
            @RequestParam(required = false) Integer rentCostMax,
            @RequestParam(required = false) Integer monthlyCostMin,
            @RequestParam(required = false) Integer monthlyCostMax
    ) {
        List<RecruitResponse> result = recruitService.getfilterRecruitPosts(latitude, longitude, radius, recruitCount,
                rentCostMin, rentCostMax, monthlyCostMin, monthlyCostMax);
        return ApiResponse.success("해당 내역의 모든 구인글 조회 완료", "RECRUIT_FILTER_GET_ALL", result);
    }

}
