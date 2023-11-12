package hyeon.buddy.service;

import hyeon.buddy.domain.User;
import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("회원가입 성공")
    void testSignupSuccessful() {
        // given
        UserSignUpRequestDTO requestDTO = new UserSignUpRequestDTO(
                "username", "test@example.com", "password12");

        // when
        when(userRepository.findByAccount(any())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        ExceptionResponse response = userService.signup(requestDTO);

        // then
        assertEquals(ExceptionCode.USER_CREATED.getCode(), response.getCode());
    }

    @Test
    @DisplayName("중복된 계정 - 회원가입")
    void testSignupDuplicateAccount() {
        // given
        UserSignUpRequestDTO requestDTO = new UserSignUpRequestDTO(
                "existingAccount", "test@example.com", "password");

        // when : 유저가 이미 존재
        when(userRepository.findByAccount(any())).thenReturn(Optional.of(new User()));
        CustomException exception = assertThrows(CustomException.class, () -> userService.signup(requestDTO));

        // then
        assertEquals(ExceptionCode.USER_DUPLICATED_ACCOUNT, exception.getExceptionCode());
    }

}