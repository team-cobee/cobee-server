package org.cobee.server.publicProfile.dto;

import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

public record PublicProfileRequestDto(
        String info,
        Lifestyle lifestyle,
        Personality personality,
        Boolean isSmoking,
        Boolean isSnoring,
        Boolean hasPet
) {
}