package org.cobee.server.publicProfile.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.publicProfile.dto.PublicProfileRequestDto;
import org.cobee.server.publicProfile.service.PublicProfileService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Map<String, String>> createPublicProfile(@RequestHeader("userId") Long userId, @RequestBody PublicProfileRequestDto requestDto) {
        publicProfileService.createPublicProfile(userId, requestDto);
        return ResponseEntity.ok(Map.of("message", "Public profile created successfully"));
    }

    @GetMapping
    public ResponseEntity<PublicProfileResponseDto> getPublicProfile(@RequestHeader("userId") Long userId) {
        return ResponseEntity.ok(publicProfileService.getPublicProfile(userId));
    }

    @PatchMapping
    public ResponseEntity<Map<String, String>> updatePublicProfile(@RequestHeader("userId") Long userId, @RequestBody PublicProfileUpdateRequestDto requestDto) {
        publicProfileService.updatePublicProfile(userId, requestDto);
        return ResponseEntity.ok(Map.of("message", "Public profile modified successfully"));
    }
}
