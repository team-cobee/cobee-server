package org.cobee.server.map.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.map.dto.LocationRequestDto;
import org.cobee.server.map.dto.LocationResponseDto;
import org.cobee.server.map.service.LocationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<LocationResponseDto> save(@RequestBody LocationRequestDto dto) {
        LocationResponseDto saved = locationService.saveLocation(dto);
        return ResponseEntity.ok(saved);
    }
}

