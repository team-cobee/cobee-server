package org.cobee.server.recruit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApplyAcceptResultEvent {
    private Long applyId;
    private Long postId;
    private Long fromUserId;
    private Long toUserId;
    private boolean accepted;
}