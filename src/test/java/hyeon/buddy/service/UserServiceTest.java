package hyeon.buddy.service;

import hyeon.buddy.domain.User;
import hyeon.buddy.dto.TokenDTO;
import hyeon.buddy.dto.UserSignInRequestDTO;
import hyeon.buddy.dto.UserSignInResponseDTO;
import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.UserRepository;
import hyeon.buddy.security.TokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private RedisService redisService;

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
        ExceptionResponse response = userService.signUp(requestDTO);

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
        CustomException exception = assertThrows(CustomException.class, () -> userService.signUp(requestDTO));

        // then
        assertEquals(ExceptionCode.USER_DUPLICATED_ACCOUNT, exception.getExceptionCode());
    }

    @Test
    @DisplayName("로그인 성공")
    public void SignIn() {

        UserSignUpRequestDTO dto = new UserSignUpRequestDTO(
                "username", "test@example.com", "password12");
        User user = User.from(dto);

        TokenDTO tokenDto = TokenDTO.builder()
                .userId(user.getId())
                .accessToken("token")
                .refreshToken("token")
                .build();

        // when
        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenProvider.createToken(user.getId(), Boolean.TRUE)).thenReturn("token");
        //when(redisService.saveRefreshToken(user.getId(), tokenDto.getRefreshToken())).thenReturn(true);
        UserSignInResponseDTO responseDto = userService.signIn(
                new UserSignInRequestDTO(user.getEmail(), user.getPassword()));

        // then
        assertThat(responseDto.getCode()).isEqualTo(ExceptionCode.USER_LOGIN_SUCCEED.getCode());
    }

    @Test
    @DisplayName("RT 만료 - 토큰 재발급")
    public void testCheckRefreshToken_Null() {
        // Given
        String refresh = "refresh_token";
        Long id = 1L;
        when(redisService.getRefreshToken(id)).thenReturn(null);

        // When & Then
        try {
            userService.checkRefreshToken(refresh, id);
        } catch (CustomException e) {
            // Then
            assertEquals(ExceptionCode.TOKEN_NOT_FOUND, e.getExceptionCode());
        }
    }

}