package org.cobee.server.global.error.handler;

import org.cobee.server.global.error.code.ErrorCode;
import org.cobee.server.global.error.exception.CustomException;
import org.cobee.server.global.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // CustomException 처리
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
//        ErrorCode errorCode = ex.getErrorCode();
//        return ResponseEntity
//                .status(errorCode.getStatus())
//                .body(ApiResponse.failure(
//                        errorCode.getMessage(),
//                        errorCode.getCode(),
//                        ex.getMessage()
//                ));
//    }
    @ExceptionHandler(CustomException.class)
    public ApiResponse<Void> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return ApiResponse.failure(
                errorCode.getMessage(),
                errorCode.getCode(),
                ex.getMessage()
        );
    }

    // 서버 내부 오류 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpectedException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "Internal Server Error",
                        "500",
                        ex.getMessage()
                ));
    }
}
