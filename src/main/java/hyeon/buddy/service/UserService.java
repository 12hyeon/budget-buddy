package hyeon.buddy.service;

import hyeon.buddy.dto.UserSignInRequestDTO;
import hyeon.buddy.dto.UserSignInResponseDTO;
import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;

public interface UserService {

    ExceptionResponse signUp(UserSignUpRequestDTO dto);
    UserSignInResponseDTO signIn(UserSignInRequestDTO dto);

}
