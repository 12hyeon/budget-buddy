package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignInResponseDTO {
    private ExceptionCode exceptionCode;
    private TokenDTO token;
}
