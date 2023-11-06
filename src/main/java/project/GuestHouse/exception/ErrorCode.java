package project.GuestHouse.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // API
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, 409, "이미 가입한 사용자 email 입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, 401, "올바른 비밀번호가 아닙니다."),
    NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "사용자 닉네임이 존재하지 없습니다."),
    USER_EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자 email 입니다."),
    USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "존재하지 않는 사용자 id 입니다."),
    // JWT
    TOKEN_EXPIRED_ERROR(HttpStatus.UNAUTHORIZED,401,"토큰이 만료되었습니다"),
    TOKEN_SIGNATURE_ERROR(HttpStatus.UNAUTHORIZED,401,"유효하지 않은 토큰입니다."),
    TOKEN_NOT_EXIST(HttpStatus.NOT_FOUND,404,"토큰이 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final int code;
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
