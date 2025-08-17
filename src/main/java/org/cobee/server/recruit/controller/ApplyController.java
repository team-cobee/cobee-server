package org.cobee.server.recruit.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.recruit.dto.ApplyRequest;
import org.cobee.server.recruit.dto.ApplyResponse;
import org.cobee.server.recruit.service.ApplyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apply")
public class ApplyController {
    private final ApplyService applyService;
    @PostMapping("/{fromMemberId}")
    public ApiResponse<ApplyResponse> applyForRecruit(@PathVariable(name="fromMemberId") Long memberId,
                                                      @RequestBody ApplyRequest request){
        ApplyResponse result = applyService.apply(memberId, request);
        return ApiResponse.success("", "", result);

    }
}
