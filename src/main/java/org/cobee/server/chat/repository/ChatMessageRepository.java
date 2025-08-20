package org.cobee.server.chat.repository;

import java.util.List;
import org.cobee.server.chat.document.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdOrderByTimestampAsc(Long chatRoomId);

}