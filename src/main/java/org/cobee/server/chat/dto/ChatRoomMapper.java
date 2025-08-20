package org.cobee.server.chat.dto;

import java.util.List;
import java.util.stream.Collectors;
import org.cobee.server.chat.document.ChatMessage;
import org.cobee.server.chat.domain.ChatRoom;

public class ChatRoomMapper {

    public static ChatRoomResponseDto toDto(ChatRoom room) {
        return ChatRoomResponseDto.builder()
                .id(room.getId())
                .name(room.getName())
                .maxMemberCount(room.getMaxMemberCount())
                .currentUserCount(room.getCurrentUserCount())
                .build();
    }

    public static List<ChatRoomResponseDto> toRoomDtoList(List<ChatRoom> rooms) {
        return rooms.stream().map(ChatRoomMapper::toDto).collect(Collectors.toList());
    }

    public static ChatMessageResponseDto toDto(ChatMessage m) {
        return ChatMessageResponseDto.builder()
                .id(m.getId())
                .roomId(m.getChatRoomId())
                .sender(m.getSenderId())
                .senderUsername(m.getSenderUsername())
                .message(m.getMessage())
                .timestamp(m.getTimestamp())
                .messageType(m.getMessageType() != null ? m.getMessageType().name() : null)
                .imageUrl(m.getImageUrl())
                .build();
    }

    public static List<ChatMessageResponseDto> toMessageDtoList(List<ChatMessage> messages) {
        return messages.stream().map(ChatRoomMapper::toDto).collect(Collectors.toList());
    }
}
