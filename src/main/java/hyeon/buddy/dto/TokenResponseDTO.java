package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public
class TokenResponseDTO extends ExceptionResponse {
    private final TokenDTO token;

    public TokenResponseDTO(ExceptionCode exceptionCode, TokenDTO token) {
        super(exceptionCode);
        this.token = token;
    }
}
