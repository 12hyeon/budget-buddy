package hyeon.buddy.service;

import hyeon.buddy.domain.User;
import hyeon.buddy.dto.UserSignUpRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.UserRepository;
import hyeon.buddy.utility.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입
     *
     * @param dto 사용자가 입력한 정보
     * @return 회원가입 시, 발생한 문제 또는 성공 여부 반환
     */
    @Override
    @Transactional
    public ExceptionResponse signup(UserSignUpRequestDTO dto) {

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

}
