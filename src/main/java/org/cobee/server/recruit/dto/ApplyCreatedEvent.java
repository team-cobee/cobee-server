package org.cobee.server.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplyCreatedEvent {
    private Long applyId;
    private Long postId;
    private Long fromUserId;
    private Long toUserId;
}