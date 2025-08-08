package org.cobee.server.publicProfile.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.publicProfile.dto.PublicProfileRequestDto;
import org.cobee.server.publicProfile.service.PublicProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.cobee.server.publicProfile.dto.PublicProfileResponseDto;
import org.cobee.server.publicProfile.dto.PublicProfileUpdateRequestDto;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
public class PublicProfileController {

    private final PublicProfileService publicProfileService;

    @PostMapping()
    public ResponseEntity<Map<String, String>> createPublicProfile(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestBody PublicProfileRequestDto requestDto) {
        Long memberId = principalDetails.getMember().getId();
        publicProfileService.createPublicProfile(memberId, requestDto);
        return ResponseEntity.ok(Map.of("message", "Public profile created successfully"));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<PublicProfileResponseDto> getPublicProfile(@PathVariable("memberId") Long memberId) {
        return ResponseEntity.ok(publicProfileService.getPublicProfile(memberId));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<Map<String, String>> updatePublicProfile(@PathVariable("memberId") Long memberId, @RequestBody PublicProfileUpdateRequestDto requestDto) {
        publicProfileService.updatePublicProfile(memberId, requestDto);
        return ResponseEntity.ok(Map.of("message", "Public profile modified successfully"));
    }
}
