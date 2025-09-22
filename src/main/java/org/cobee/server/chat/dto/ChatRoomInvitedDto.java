package org.cobee.server.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomInvitedDto {
    private Long chatRoomId;
    private Long postId;
    private Long fromUserId;
    private Long toUserId;
    private String chatRoomName;
}