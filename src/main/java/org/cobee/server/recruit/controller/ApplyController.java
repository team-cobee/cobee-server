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
    @PostMapping("/{fromMember}")  // fromMember가 reqest.postId에 지원함
    public ApiResponse<ApplyResponse> applyForPost(@PathVariable(name="fromMember") Long fromMember,
                                                   @RequestBody ApplyRequest request){
        ApplyResponse result = applyService.applyForRecruit(request);
        return ApiResponse.success("", "", result);
    }
}
