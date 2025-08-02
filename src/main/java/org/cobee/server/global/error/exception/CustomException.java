package org.cobee.server.global.error.exception;

import lombok.Getter;
import org.cobee.server.global.error.code.ErrorCode;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    /* throw new CustomException(ErrorCode.XXX)으로 에러 던지기 */
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
