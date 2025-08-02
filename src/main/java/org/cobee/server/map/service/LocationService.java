package org.cobee.server.map.service;

import lombok.RequiredArgsConstructor;
import org.cobee.server.map.domain.Location;
import org.cobee.server.map.dto.LocationRequestDto;
import org.cobee.server.map.dto.LocationResponseDto;
import org.cobee.server.map.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final GoogleMapService googleMapService;
    private final LocationRepository locationRepository;

    public LocationResponseDto saveLocation(LocationRequestDto dto) {

        String address = googleMapService.getAddressFromCoordinates(dto.getLatitude(), dto.getLongitude());

        Location location = Location.builder()
                .address(address)
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .build();

        Location saved = locationRepository.save(location);

        return LocationResponseDto.builder()
                .id(saved.getId())
                .address(saved.getAddress())
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .createdAt(saved.getCreatedAt())
                .build();
    }
}
