package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomCreateRequestDto {
    private String name;
    private Long postId;
}
