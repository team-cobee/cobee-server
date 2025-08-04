package org.cobee.server.map.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NearbyPlaceDto {
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
}

