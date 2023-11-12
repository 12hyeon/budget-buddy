package hyeon.buddy.service;

import hyeon.buddy.domain.User;
import hyeon.buddy.dto.*;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.UserRepository;
import hyeon.buddy.security.TokenProvider;
import hyeon.buddy.utility.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    /**
     * 회원가입
     *
     * @param dto 사용자가 입력한 정보
     * @return 결과 코드
     */
    @Override
    @Transactional
    public ExceptionResponse signUp(UserSignUpRequestDTO dto) {

        String password = dto.getPassword();

        // 계정 중복 체크
        userRepository.findByAccount(dto.getAccount())
                .ifPresent(user -> {
                    throw new CustomException(ExceptionCode.USER_DUPLICATED_ACCOUNT);
                });

        // 이메일 중복 체크
        userRepository.findByEmail(dto.getEmail())
                .ifPresent(user -> {
                    throw new CustomException(ExceptionCode.USER_DUPLICATED_EMAIL);
                });

        // 비밀번호 유효성 체크
        validatePassword(password);

        // 비밀번호 암호화
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(User.from(dto));

        return new ExceptionResponse(ExceptionCode.USER_CREATED);
    }

    /**
     * 4가지 조건에 대한 비밀번호의 유효성 검사
     *
     * @param password 비밀번호
     */
    private void validatePassword(String password) {
        if (Validation.isUsualPassword(password)) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD_USUAL_PASSWORD);
        }

        if (Validation.hasSameCharacters(password)) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD_SAME_CHARACTERS);
        }

        if (Validation.hasConsecutiveCharacters(password)) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD_CONSECUTIVE_CHARACTERS);
        }

        if (Validation.containsTwoOrMoreCharacterTypes(password)) {
            throw new CustomException(ExceptionCode.INVALID_PASSWORD_AT_LEAST_2_TYPES);
        }
    }

    /**
     * 로그인
     *
     * @param dto 사용자가 입력한 정보
     * @return 결과 코드
     */
    @Override
    @Transactional
    public UserSignInResponseDTO signIn(UserSignInRequestDTO dto) {

        // 이메일 및 비밀번호 확인
        User user = userRepository.findByEmail(dto.getEmail())
                .filter(u -> passwordEncoder.matches(dto.getPassword(), u.getPassword()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_LOGIN_FAILED));

        // token 발급
        TokenDTO tokenDto = createTokens(user.getId());

        // id 기준 RT를 redis에 저장
        redisService.saveRefreshToken(user.getId(), tokenDto.getRefreshToken());

        return new UserSignInResponseDTO(ExceptionCode.USER_LOGIN_SUCCEED, tokenDto);
    }

    public TokenDTO createTokens(Long userId) { // token 발급

        String accessToken = tokenProvider.createToken( userId, Boolean.FALSE); // access
        String refreshToken = tokenProvider.createToken(userId, Boolean.TRUE); // refresh

        return TokenDTO.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * 토큰 재발급 또는 재 로그인 요청
     *
     * @param id 요청한 유저의 id
     * @return 재발급된 token
     */
    @Override
    @Transactional
    public TokenResponseDTO reissue(String refresh, Long id) {

        // redis : 10일, RT : 20일
        // RT가 기한이 남고 redis TTL이 지난 경우, 재 로그인 요청
        checkRefreshToken(refresh, id);

        TokenDTO tokenDto = createTokens(id);

        // redis에 새로운 RT 저장
        redisService.saveRefreshToken(id, tokenDto.getRefreshToken());

        return new TokenResponseDTO(ExceptionCode.TOKEN_REISSUED, tokenDto);
    }

    public void checkRefreshToken(String refresh, Long id) {
        String getToken = redisService.getRefreshToken(id);

        if (getToken == null) {
            throw new CustomException(ExceptionCode.TOKEN_NOT_FOUND);
        }
        if (!getToken.equals(refresh)) {
            throw new CustomException(ExceptionCode.TOKEN_NOT_FOUND);
        }
    }

}
