package org.cobee.server.chat.document;


import com.mongodb.lang.Nullable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cobee.server.chat.domain.enums.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chat_messages")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    private String id;

    private Long chatRoomId;

    private Long senderId;

    private String senderUsername;

    @Nullable
    private String message;

    private MessageType messageType;

    private String imageUrl;

    private LocalDateTime timestamp;

    private Set<Long> readBy = new HashSet<>();

    public ChatMessage(Long chatRoomId, Long senderId, String senderUsername, String message) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.message = message;
        this.messageType = MessageType.TEXT;
        this.timestamp = LocalDateTime.now();
    }

    public ChatMessage(Long chatRoomId, Long senderId, String senderUsername, String imageUrl, String message) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.senderUsername = senderUsername;
        this.imageUrl = imageUrl;
        this.message = message;
        this.messageType = MessageType.IMAGE;
        this.timestamp = LocalDateTime.now();
    }
}