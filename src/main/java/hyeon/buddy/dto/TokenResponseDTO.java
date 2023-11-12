package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public
class TokenResponseDTO {
    private ExceptionCode code;
    private TokenDTO token;
}
