package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.RecruitRequest;
import org.cobee.server.recruit.dto.RecruitResponse;
import org.cobee.server.recruit.service.RecruitService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecruitController {

    private final RecruitService recruitService;
    @PostMapping("/recruits/{memberId}")
    public ApiResponse<RecruitResponse> createRecruitPost(@RequestBody RecruitRequest request, @PathVariable(name="memberId") Long memberId){
        RecruitResponse result = recruitService.createRecruitPost(request, memberId);
        return ApiResponse.success("구인글 생성 완료", "RECRUIT_CREATED", result);
    }
}
