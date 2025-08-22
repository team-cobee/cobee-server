package org.cobee.server.map.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cobee.server.global.response.ApiResponse;
import org.cobee.server.map.dto.LocationRequestDto;
import org.cobee.server.map.dto.LocationResponseDto;
import org.cobee.server.map.dto.NearbyPlaceDto;
import org.cobee.server.map.service.GoogleMapService;
import org.cobee.server.map.service.LocationService;
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
    public ApiResponse<LocationResponseDto> save(@RequestBody LocationRequestDto dto) {
        LocationResponseDto saved = locationService.saveLocation(dto);
        return ApiResponse.success("위치 저장 성공", "LOCATION-001", saved);
    }

    // 구글 맵에서 위도, 경도로 주소 받아오기 (distance 기준으로)
    @PostMapping("/search/google/nearby")
    public ApiResponse<List<NearbyPlaceDto>> getNearbyFromGoogle(@RequestBody LocationRequestDto dto) {
        List<NearbyPlaceDto> places = googleMapService.getNearbyPlaces(
                dto.getLatitude(), dto.getLongitude(), dto.getDistance());
        return ApiResponse.success("구글 맵에서 근처 장소 조회 성공", "LOCATION-002", places);
    }

    // db 에서 위도, 경도로 주소 받아오기 (distance 기준으로)
    @PostMapping("/search/nearby/locations")
    public ApiResponse<List<LocationResponseDto>> getNearbyLocations(@RequestBody LocationRequestDto dto) {
        List<LocationResponseDto> locations = locationService.findNearbyLocations(dto);
        return ApiResponse.success("db에서 근처 위치 조회 성공", "LOCATION-003", locations);
    }
}

