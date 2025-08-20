package org.cobee.server.chat.controller;

import lombok.RequiredArgsConstructor;
import org.cobee.server.chat.dto.ChatMessageDto;
import org.cobee.server.chat.dto.MessageReadDto;
import org.cobee.server.chat.service.ChatService;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // 채팅 메시지 전송
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(ChatMessageDto chatMessageDto) {
        chatService.saveMessage(chatMessageDto);
        String destination = "/topic/room/" + chatMessageDto.getRoomId();
        messagingTemplate.convertAndSend(destination, chatMessageDto);
    }

    // 채팅 읽은 사람 목록
    @MessageMapping("/chat/readMessage")
    public void readMessage(MessageReadDto readDto) {
        chatService.markMessageAsRead(readDto.getMessageId(), readDto.getUserId());
        String destination = "/topic/room/" + readDto.getRoomId() + "/read";
        messagingTemplate.convertAndSend(destination, readDto);
    }

    //채팅 삭제 (해당 채팅방 내의 채팅)
    @MessageMapping("/chat/deleteMessage")
    public void deleteMessage(String messageId) {
        Long roomId = chatService.getRoomIdByMessageId(messageId)
                .orElseThrow(() -> new CustomException(ErrorCode.MESSAGE_NOT_FOUND));

        chatService.deleteMessage(messageId);

        messagingTemplate.convertAndSend(
                "/topic/room/" + roomId + "/messageDeleted",
                messageId
        );
    }


}
