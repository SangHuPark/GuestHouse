package project.GuestHouse.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GuestException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    @Builder
    public GuestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return errorCode.getMessage()
                + this.message;
    }
}
