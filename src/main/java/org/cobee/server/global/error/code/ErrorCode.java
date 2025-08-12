package org.cobee.server.global.error.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH-001", "Unauthorized access"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH-002", "Cannot find Member"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-001", "Cannot find Member"),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST-001", "Cannot find RecruitPost"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-001", "Cannot find Comment"),
    
    // UserPreferences 관련 에러
    USER_PREFERENCES_NOT_FOUND(HttpStatus.NOT_FOUND, "PREFERENCE-001", "Cannot find UserPreferences"),
    ALREADY_EXISTS_ERROR(HttpStatus.BAD_REQUEST, "PREFERENCE-002", "UserPreferences already exists"),
    INVALID_AGE_RANGE(HttpStatus.BAD_REQUEST, "PREFERENCE-003", "Invalid age range: minAge must be less than or equal to maxAge"),
    INVALID_COHABITANT_COUNT_RANGE(HttpStatus.BAD_REQUEST, "PREFERENCE-004", "Invalid cohabitant count range: minCount must be less than or equal to maxCount");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
