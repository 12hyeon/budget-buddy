package hyeon.buddy.service;

import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;

public interface UserService {

    ExceptionResponse signup(UserSignUpRequestDTO dto);
}
