package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public class UserSignInResponseDTO extends ExceptionResponse {
    private final TokenDTO token;

    public UserSignInResponseDTO(ExceptionCode exceptionCode, TokenDTO token) {
        super(exceptionCode);
        this.token = token;
    }
}
