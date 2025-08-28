package org.cobee.server.chat.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cobee.server.chat.domain.ChatRoom;
import org.cobee.server.chat.dto.ChatRoomCreateRequestDto;
import org.cobee.server.chat.repository.ChatRoomRepository;
import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.member.domain.Member;
import org.cobee.server.member.repository.MemberRepository;
import org.cobee.server.recruit.domain.RecruitPost;
import org.cobee.server.recruit.repository.RecruitPostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final RecruitPostRepository recruitPostRepository;

    @Transactional
    public ChatRoom createChatRoom(ChatRoomCreateRequestDto request, Member host) {
        RecruitPost post = recruitPostRepository.findById(request.getPostId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        ChatRoom newRoom = ChatRoom.builder()
                .name(request.getName())
                .maxMemberCount(request.getMaxUserCount())
                .host(host)
                .post(post)
                .build();

        return chatRoomRepository.save(newRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoom> findAllRooms() {
        return chatRoomRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ChatRoom findRoomById(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    }

    @Transactional
    public ChatRoom addUserToRoom(Long roomId, Long userId) {
        ChatRoom room = findRoomById(roomId);
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getChatRoom() != null) {
            throw new CustomException(ErrorCode.CHAT_ROOM_USER_ALREADY_EXISTS);
        }

        if (room.getCurrentUserCount() >= room.getMaxMemberCount()) {
            throw new CustomException(ErrorCode.CHAT_ROOM_FULL);
        }

        room.addUser(user);
        return chatRoomRepository.save(room);
    }

    @Transactional
    public void deleteChatRoom(Long roomId) {
        try {
            ChatRoom room = findRoomById(roomId);
            if (room == null) {
                throw new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND);
            }

            if (room.getCurrentUserCount() == 0) {
                chatRoomRepository.delete(room);
                log.info("채팅방 정상 삭제, roomId=" + roomId);
            } else {
                throw new CustomException(ErrorCode.CHAT_ROOM_EXISTS_USER);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("채팅방 삭제 실패: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("알 수 없는 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    public void removeUserFromRoom(Long roomId, Long userId) {
        ChatRoom room = findRoomById(roomId);
        Member user = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (user.getChatRoom() == null || !user.getChatRoom().equals(room)) {
            throw new CustomException(ErrorCode.CHAT_ROOM_USER_NOT_IN_ROOM);
        }

        room.removeUser(user);
        chatRoomRepository.save(room);
    }

    @Transactional
    public ChatRoom updateChatRoom(Long roomId, ChatRoomCreateRequestDto request) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!StringUtils.hasText(request.getName())) {
            throw new CustomException(ErrorCode.CHAT_ROOM_NAME_CANNOT_EMPTY);
        }

        room.editChatroomName(request.getName());
        return chatRoomRepository.save(room);
    }

    @Transactional
    public void outUserFromRoom(Long roomId, Member host, Long userIdToOut) {
        ChatRoom room = findRoomById(roomId);

        if (room.getHost() == null || !room.getHost().getId().equals(host.getId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Member memberToOut = memberRepository.findById(userIdToOut)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (memberToOut.getChatRoom() == null || !memberToOut.getChatRoom().getId().equals(roomId)) {
            throw new CustomException(ErrorCode.CHAT_ROOM_USER_NOT_IN_ROOM);
        }

        room.removeUser(memberToOut);
        chatRoomRepository.save(room);
    }
}
