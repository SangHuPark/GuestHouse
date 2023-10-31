package project.GuestHouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "이미 가입한 사용자 email 입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "올바른 비밀번호가 아닙니다."),
    NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 닉네임이 존재하지 없습니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 email 입니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자 입니다.");


    private final HttpStatus httpStatus;
    private final String message;

    /*
    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ErrorResult getErrorResult() {
        return new ErrorResult(this, this.message);
    }
    */
}
