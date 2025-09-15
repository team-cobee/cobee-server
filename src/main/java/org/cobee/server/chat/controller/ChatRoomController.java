package org.cobee.server.chat.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.cobee.server.auth.service.PrincipalDetails;
import org.cobee.server.chat.document.ChatMessage;
import org.cobee.server.chat.domain.ChatRoom;
import org.cobee.server.chat.dto.ChatRoomCreateRequestDto;
import org.cobee.server.chat.dto.ChatRoomMapper;
import org.cobee.server.chat.dto.ChatRoomResponseDto;
import org.cobee.server.chat.dto.ChatMessageResponseDto;
import org.cobee.server.chat.dto.ChatRoomUserListResponseDto;
import org.cobee.server.chat.dto.JoinRoomRequestDto;
import org.cobee.server.chat.service.ChatRoomService;
import org.cobee.server.chat.service.ChatService;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/chat")
@RestController
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    //채팅방 개설
    @PostMapping("/rooms")
    public ApiResponse<ChatRoomResponseDto> createRoom(
            @RequestBody ChatRoomCreateRequestDto request,
            @AuthenticationPrincipal PrincipalDetails principal) {
        ChatRoom room = chatRoomService.createChatRoom(request, principal.getMember());
        chatRoomService.addUserToRoom(room.getId(), principal.getMember().getId());
        return ApiResponse.success("채팅방 생성 성공", "CHAT_ROOM_CREATED", ChatRoomMapper.toDto(room));
    }


    //채팅방 이름 수정
    @PatchMapping("/rooms/{roomId}")
    public ApiResponse<ChatRoomResponseDto> updateRoom(@PathVariable Long roomId,
                                                       @RequestBody ChatRoomCreateRequestDto request) {
        try {
            ChatRoom updatedRoom = chatRoomService.updateChatRoom(roomId, request);
            return ApiResponse.success("채팅방 이름 수정 성공", "CHAT_ROOM_UPDATED", ChatRoomMapper.toDto(updatedRoom));
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("채팅방을 찾을 수 없습니다", "CHAT_ROOM_NOT_FOUND", e.getMessage());
        }
    }

    //모든 채팅방 목록 조회
    @GetMapping("/rooms")
    public ApiResponse<List<ChatRoomResponseDto>> getAllRooms() {
        List<ChatRoom> rooms = chatRoomService.findAllRooms();
        return ApiResponse.success("채팅방 목록 조회 성공", "CHAT_ROOMS_LIST", ChatRoomMapper.toRoomDtoList(rooms));
    }

    //채팅방 참여
    @PostMapping("/rooms/join/{roomId}")
    public ApiResponse<ChatRoomResponseDto> joinRoom(
            @PathVariable Long roomId,
            @RequestBody JoinRoomRequestDto req
    ) {
        try {
            ChatRoom updatedRoom = chatRoomService.addUserToRoom(roomId, req.getUserId());
            return ApiResponse.success("채팅방 참여 성공", "CHAT_ROOM_JOINED", ChatRoomMapper.toDto(updatedRoom));
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("채팅방을 찾을 수 없습니다", "CHAT_ROOM_NOT_FOUND", e.getMessage());
        } catch (IllegalStateException e) {
            return ApiResponse.failure("이미 채팅방에 참여 중입니다", "CHAT_ROOM_ALREADY_JOINED", e.getMessage());
        }
    }


    //특정 채팅방의 채팅 기록 조회
    @GetMapping("/rooms/history/{roomId}")
    public ApiResponse<List<ChatMessageResponseDto>> getChatHistory(@PathVariable Long roomId) {
        List<ChatMessage> history = chatService.getChatHistory(roomId);
        return ApiResponse.success("채팅 기록 조회 성공", "CHAT_HISTORY", ChatRoomMapper.toMessageDtoList(history));
    }
    //내가 속한 채팅방 조회
    @GetMapping("/rooms/my")
    public ApiResponse<ChatRoomResponseDto> getMyRoom(@AuthenticationPrincipal PrincipalDetails principal) {
        Optional<ChatRoom> myRoom = chatRoomService.findRoomByUser(principal.getMember());
        if (myRoom.isPresent()) {
            return ApiResponse.success("내 채팅방 조회 성공", "MY_CHAT_ROOM", ChatRoomMapper.toDto(myRoom.get()));
        } else {
            return ApiResponse.failure("속한 채팅방이 없습니다", "NO_CHAT_ROOM", "No chat room found for the user");
        }
    }
    //채팅방 내의 유저 목록 조회
    @GetMapping("/rooms/{roomId}/users")
    public ApiResponse<List<ChatRoomUserListResponseDto>> getUsersInRoom(@PathVariable Long roomId) {
        try {
            List<ChatRoomUserListResponseDto> users = chatRoomService.getUsernamesInRoom(roomId);
            return ApiResponse.success("채팅방 유저 목록 조회 성공", "CHAT_ROOM_USERS", users);
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("채팅방을 찾을 수 없습니다", "CHAT_ROOM_NOT_FOUND", e.getMessage());
        }
    }
    //채팅방 나가기
    @PostMapping("/rooms/exit/{roomId}")
    public ApiResponse<Void> exitRoom(@PathVariable Long roomId, @RequestBody Map<String, Object> payload) {
        Object userIdObj = payload.get("userId");
        if (userIdObj == null) {
            return ApiResponse.failure("유저 ID가 필요합니다", "USER_ID_REQUIRED", "userId is required");
        }

        Long userId;
        try {
            userId = Long.valueOf(userIdObj.toString());
        } catch (NumberFormatException e) {
            return ApiResponse.failure("유효하지 않은 유저 ID", "INVALID_USER_ID", "userId must be a valid number");
        }

        try {
            chatRoomService.removeUserFromRoom(roomId, userId);
            return ApiResponse.success("채팅방 나가기 성공", "CHAT_ROOM_EXITED");
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("채팅방을 찾을 수 없습니다", "CHAT_ROOM_NOT_FOUND", e.getMessage());
        }
    }

    //채팅방 삭제
    @DeleteMapping("/rooms/{roomId}")
    public ApiResponse<Void> deleteRoom(@PathVariable Long roomId) {
        try {
            chatRoomService.deleteChatRoom(roomId);
            return ApiResponse.success("채팅방 삭제 성공", "CHAT_ROOM_DELETED");
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("채팅방을 찾을 수 없습니다", "CHAT_ROOM_NOT_FOUND", e.getMessage());
        }
    }
    // 채팅방에서 유저 강퇴 (채팅방 방장인 host만 가능)
    @DeleteMapping("/rooms/{roomId}/users/{userId}")
    public ApiResponse<Void> outUser(
            @PathVariable Long roomId,
            @PathVariable Long userId,
            @AuthenticationPrincipal PrincipalDetails principal) {
        chatRoomService.outUserFromRoom(roomId, principal.getMember(), userId);
        return ApiResponse.success("유저 강퇴 성공", "USER_KICKED");
    }
}
