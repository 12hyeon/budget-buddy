package hyeon.buddy.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public CustomException(ExceptionCode e) {
        super(e.getMessage());
        this.exceptionCode = e;
    }
}