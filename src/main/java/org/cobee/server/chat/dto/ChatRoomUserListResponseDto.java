package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomUserListResponseDto {
    private Long id;
    private String name;
    private boolean isHost;
}
