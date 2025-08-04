package org.cobee.server.map.service;

import java.util.List;
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

        if(locationRepository.existsByLatitudeAndLongitude(dto.getLatitude(), dto.getLongitude())) {
            throw new IllegalArgumentException("이미 저장된 위치입니다.");
        }

        Location saved = locationRepository.save(location);

        return LocationResponseDto.builder()
                .id(saved.getId())
                .address(saved.getAddress())
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<LocationResponseDto> findNearbyLocations(LocationRequestDto dto) {
        double lat = dto.getLatitude();
        double lng = dto.getLongitude();
        double radiusKm = dto.getDistance() / 1000.0;

        List<Location> locations = locationRepository.findWithinDistance(lat, lng, radiusKm);

        return locations.stream()
                .map(loc -> LocationResponseDto.builder()
                        .id(loc.getId())
                        .address(loc.getAddress())
                        .latitude(loc.getLatitude())
                        .longitude(loc.getLongitude())
                        .createdAt(loc.getCreatedAt())
                        .build())
                .toList();
    }

}
