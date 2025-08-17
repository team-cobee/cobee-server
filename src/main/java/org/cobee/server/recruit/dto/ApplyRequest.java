package org.cobee.server.recruit.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApplyRequest {
    private Long postId;
}
