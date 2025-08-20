package org.cobee.server.chat.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cobee.server.chat.domain.ChatRoom;
import org.cobee.server.chat.dto.ChatRoomCreateRequestDto;
import org.cobee.server.chat.repository.ChatRoomRepository;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ChatRoom createChatRoom(ChatRoomCreateRequestDto request) {
        ChatRoom newRoom = new ChatRoom(request.getName(), request.getMaxUserCount());
        return chatRoomRepository.save(newRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("해당 채팅방을 찾을 수 없음. " + roomId));
    }

    @Transactional
    public ChatRoom addUserToRoom(Long roomId, String username) {
        ChatRoom room = findRoomById(roomId);
        Member user= memberRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없음: " + username));

        if (user.getChatRoom() != null) {
            throw new IllegalStateException("해당 사용자는 이미 채팅방에 있어요.");
        }
        if (room.getCurrentUserCount() >= room.getMaxMemberCount()) {
            throw new IllegalStateException("Chat room is full.");
        }

        room.addUser(user);
        return chatRoomRepository.save(room);
    }


}
