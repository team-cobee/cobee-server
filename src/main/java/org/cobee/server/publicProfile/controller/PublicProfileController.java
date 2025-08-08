package org.cobee.server.publicProfile.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.publicProfile.dto.PublicProfileRequestDto;
import org.cobee.server.publicProfile.service.PublicProfileService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.publicProfile.dto.PublicProfileUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
public class PublicProfileController {

    private final PublicProfileService publicProfileService;

    @PostMapping()
    public ApiResponse<Void> createPublicProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PublicProfileRequestDto requestDto) {
        Long memberId = principalDetails.getMember().getId();
        publicProfileService.createPublicProfile(memberId, requestDto);
        return ApiResponse.success("Public profile created successfully", "201");
    }

    @GetMapping("/{memberId}")
    public ApiResponse<PublicProfileResponseDto> getPublicProfile(@PathVariable("memberId") Long memberId) {
        PublicProfileResponseDto profile = publicProfileService.getPublicProfile(memberId);
        return ApiResponse.success("Public profile retrieved successfully", "200", profile);
    }

    @PatchMapping("/{memberId}")
    public ApiResponse<Void> updatePublicProfile(@PathVariable("memberId") Long memberId, @RequestBody PublicProfileUpdateRequestDto requestDto) {
        publicProfileService.updatePublicProfile(memberId, requestDto);
        return ApiResponse.success("Public profile modified successfully", "200");
    }
}
