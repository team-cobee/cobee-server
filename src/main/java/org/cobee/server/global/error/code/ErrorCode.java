package org.cobee.server.global.error.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "CHATROOM-001", "Cannot find ChatRoom"),
    CHAT_ROOM_FULL(HttpStatus.BAD_REQUEST, "CHATROOM-002", "ChatRoom is full"),
    CHAT_ROOM_NAME_DUPLICATED(HttpStatus.BAD_REQUEST, "CHATROOM-003", "ChatRoom name is duplicated"),
    CHAT_ROOM_NOT_EDITABLE(HttpStatus.BAD_REQUEST, "CHATROOM-004", "ChatRoom is not editable"),
    CHAT_ROOM_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "CHATROOM-005", "Cannot find user in ChatRoom"),
    CHAT_ROOM_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "CHATROOM-006", "Cannot find post in ChatRoom"),
    CHAT_ROOM_USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CHATROOM-007", "User already exists in ChatRoom"),
    CHAT_ROOM_USER_NOT_IN_ROOM(HttpStatus.BAD_REQUEST, "CHATROOM-008", "User is not in the ChatRoom"),
    CHAT_ROOM_EXISTS_USER(HttpStatus.BAD_REQUEST, "CHATROOM-009", "User exists in the ChatRoom"),
    CHAT_ROOM_NAME_CANNOT_EMPTY(HttpStatus.BAD_REQUEST, "CHATROOM-010", "ChatRoom name cannot be empty"),


    LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "LOCATION-001", "Cannot find Location"),
    LOCATION_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "LOCATION-002", "Location already exists"),

    GOOGLE_MAP_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE-001", "Google Map API error"),
    GOOGLE_NEARBY_API_ERROR(HttpStatus.BAD_GATEWAY, "GOOGLE-002", "Google Nearby Search API error"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "GOOGLE-003", "Address not found"),
    GOOGLE_RESPONSE_PARSE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GOOGLE-004", "Error parsing Google API response"),
    GOOGLE_OVER_QUERY_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "GOOGLE-005", "Google API query limit exceeded"),
    GOOGLE_REQUEST_DENIED(HttpStatus.FORBIDDEN, "GOOGLE-006", "Google API request denied"),
    GOOGLE_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "GOOGLE-007", "Invalid request to Google API"),


    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE-001", "Cannot find Message"),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "Unauthorized access"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH-002", "Cannot find Member"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "Cannot find RecruitPost"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "Cannot find Comment"),
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY-001", "APPLY find Comment"),
    ALARM_NOT_CREATED(HttpStatus.BAD_REQUEST, "ALARM-001", "Alarm cannot be created");
    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
