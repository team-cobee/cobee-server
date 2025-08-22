package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.cobee.server.chat.domain.enums.MessageType;

@Getter
@Builder
public class ChatMessageDto {
    private Long roomId;
    private Long senderId;
    private String message;
    private MessageType messageType = MessageType.TEXT;
    private String imageUrl;
}
