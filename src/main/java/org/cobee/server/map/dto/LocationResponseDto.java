package org.cobee.server.map.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponseDto {
    private Long id;
    private String address;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
}
