package org.cobee.server.publicProfile.dto;

import org.cobee.server.publicProfile.domain.enums.*;

public record PublicProfileRequestDto(
        String info,
        Lifestyle lifestyle,
        Personality personality,
        Smoking isSmoking,
        Snoring isSnoring,
        Pets hasPet
) {
}