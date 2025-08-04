package org.cobee.server.map.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cobee.server.map.dto.LocationRequestDto;
import org.cobee.server.map.dto.LocationResponseDto;
import org.cobee.server.map.dto.NearbyPlaceDto;
import org.cobee.server.map.service.GoogleMapService;
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
    private final GoogleMapService googleMapService;

    // 위도, 경도로 내 위치 받아오기
    @PostMapping
    public ResponseEntity<LocationResponseDto> save(@RequestBody LocationRequestDto dto) {
        LocationResponseDto saved = locationService.saveLocation(dto);
        return ResponseEntity.ok(saved);
    }

    // 구글 맵에서 위도, 경도로 주소 받아오기 (distance 기준으로)
    @PostMapping("/search/google/nearby")
    public ResponseEntity<List<NearbyPlaceDto>> getNearbyFromGoogle(@RequestBody LocationRequestDto dto) {
        List<NearbyPlaceDto> places = googleMapService.getNearbyPlaces(
                dto.getLatitude(), dto.getLongitude(), dto.getDistance());
        return ResponseEntity.ok(places);
    }

    // db 에서 위도, 경도로 주소 받아오기 (distance 기준으로)
    @PostMapping("/search/nearby/locations")
    public ResponseEntity<List<LocationResponseDto>> getNearbyLocations(@RequestBody LocationRequestDto dto) {
        List<LocationResponseDto> locations = locationService.findNearbyLocations(dto);
        return ResponseEntity.ok(locations);
    }
}

