package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomCompleteResponseDto {
    private boolean isComplete;
    private Long postId;
}
