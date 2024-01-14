package project.GuestHouse.exception.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import project.GuestHouse.exception.errorCode.ErrorCode;
import project.GuestHouse.exception.errorCode.UserErrorCode;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
//    private String message;

    /*@Builder
    public GuestException(UserErrorCode userErrorCode) {
        this.userErrorCode = userErrorCode;
    }*/

    /*@Override
    public String toString() {
        return userErrorCode.getMessage()
                + this.message;
    }*/
}
