package org.cobee.server.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequestDto {
    private Double latitude;
    private Double longitude;
    private Integer distance;

    public LocationRequestDto(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = null;
    }
}
