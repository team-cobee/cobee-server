package org.cobee.server.chat.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponseDto {
    private String id;
    private Long roomId;
    private Long sender;
    private String senderUsername;
    private String message;
    private LocalDateTime timestamp;
    private String messageType;
    private String imageUrl;
}