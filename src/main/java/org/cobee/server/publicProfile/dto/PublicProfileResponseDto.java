package org.cobee.server.publicProfile.dto;

import org.cobee.server.publicProfile.domain.enums.Lifestyle;
import org.cobee.server.publicProfile.domain.enums.Personality;

public record PublicProfileResponseDto(
        Long userId,
        String name,
        String gender,
        String info,
        Lifestyle mLifestyle,
        Personality mPersonality,
        Boolean mSmoking,
        Boolean mSnoring,
        Boolean mPet
) {
}
