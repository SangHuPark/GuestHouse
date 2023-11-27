package project.GuestHouse.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
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
