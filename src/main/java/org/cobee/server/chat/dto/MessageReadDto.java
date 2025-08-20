package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageReadDto {
    private String messageId;
    private Long userId;
    private Long roomId;
}