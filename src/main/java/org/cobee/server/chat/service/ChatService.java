package org.cobee.server.chat.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cobee.server.chat.document.ChatMessage;
import org.cobee.server.chat.domain.enums.MessageType;
import org.cobee.server.chat.dto.ChatMessageDto;
import org.cobee.server.chat.repository.ChatMessageRepository;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    public ChatMessage saveMessage(ChatMessageDto messageDto) {
        ChatMessage chatMessage;
        String senderUsername = memberRepository.findById(messageDto.getSenderId())
                .map(member -> member.getName())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음: " + messageDto.getSenderId()));

        if (messageDto.getMessageType() == MessageType.IMAGE) {
            chatMessage = new ChatMessage(
                    messageDto.getRoomId(),
                    messageDto.getSenderId(),
                    senderUsername,
                    messageDto.getImageUrl(),
                    messageDto.getMessage()
            );
        } else {
            chatMessage = new ChatMessage(
                    messageDto.getRoomId(),
                    messageDto.getSenderId(),
                    senderUsername,
                    messageDto.getMessage()
            );
        }
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getChatHistory(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderByTimestampAsc(roomId);
    }

    public Optional<ChatMessage> markMessageAsRead(String messageId, Long userId) {
        return chatMessageRepository.findById(messageId).map(message -> {
            if (message.getReadBy().add(userId)) {
                return chatMessageRepository.save(message);
            }
            return message;
        });
    }

    public void deleteMessage(String messageId) {
        chatMessageRepository.deleteById(messageId);
    }

    @Transactional(readOnly = true)
    public Optional<Long> getRoomIdByMessageId(String messageId) {
        return chatMessageRepository.findById(messageId)
                .map(ChatMessage::getChatRoomId);
    }
}
