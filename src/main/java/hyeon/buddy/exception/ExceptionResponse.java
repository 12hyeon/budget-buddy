package hyeon.buddy.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {
    private final int status;
    private final String code;
    private final String message;

    public ExceptionResponse(ExceptionCode exceptionCode) {
        this.status = exceptionCode.getStatus().value();
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}
