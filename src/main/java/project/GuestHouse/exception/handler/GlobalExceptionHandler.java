/*
package project.GuestHouse.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import project.GuestHouse.exception.errorCode.ErrorCode;
import project.GuestHouse.exception.errorCode.UserErrorCode;
import project.GuestHouse.exception.exception.ApiException;
import project.GuestHouse.exception.response.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // RuntimeException
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleCustomException(ApiException e) {
        return handleExceptionInternal(e.getErrorCode());
    }

    // RuntimeException(Unchecked)과 대부분의 에러 처리 메세지를 보내는 메서드
    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(makeErrorResponse(errorCode));
    }

    // 에러 처리 메시지 생성 메서드 공통 처리 메서드
    private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
*/
