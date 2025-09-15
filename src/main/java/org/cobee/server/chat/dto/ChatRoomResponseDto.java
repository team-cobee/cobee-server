package org.cobee.server.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {
    private Long id;
    private String name;
    private Long postId;
    private int maxMemberCount;
    private int currentUserCount;
}
