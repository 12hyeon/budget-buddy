package hyeon.buddy.exception;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionResponse {
    private ExceptionCode exceptionCode;
    private String code;
    private String message;
}
