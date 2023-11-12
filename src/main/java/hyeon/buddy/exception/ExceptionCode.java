package hyeon.buddy.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {
    //GlobalException
    UNDEFINED_EXCEPTION(HttpStatus.BAD_REQUEST, "Z000", "알 수 없는 오류입니다."),


    // user 관련 예외 코드
    USER_CREATED(HttpStatus.CREATED, "U000","회원가입을 성공했습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001","사용자를 찾을 수 없습니다."),
    USER_DUPLICATED_ACCOUNT(HttpStatus.CONFLICT, "U002", "중복된 계정이 존재합니다."),
    USER_DUPLICATED_EMAIL(HttpStatus.CONFLICT, "U003", "중복된 이메일이 존재합니다."),

    USER_LOGIN_SUCCEED(HttpStatus.OK, "U004", "로그인을 성공했습니다."),
    USER_LOGIN_FAILED(HttpStatus.CONFLICT, "U005", "로그인을 실패했습니다."),

    // 비밀번호
    INVALID_PASSWORD_USUAL_PASSWORD(HttpStatus.BAD_REQUEST, "P001", "자주 사용되는 비밀번호는 사용할 수 없습니다."),
    INVALID_PASSWORD_AT_LEAST_2_TYPES(HttpStatus.BAD_REQUEST, "P002", "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
    INVALID_PASSWORD_SAME_CHARACTERS(HttpStatus.BAD_REQUEST, "P003", "같은 문자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다."),
    INVALID_PASSWORD_CONSECUTIVE_CHARACTERS(HttpStatus.BAD_REQUEST, "P004",
            "연속된 문자 또는 숫자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다."),

    // 토큰
    TOKEN_INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "T000", "유효하지 않은 토큰 서명 오류입니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "T001", "잘못된 토큰 오류입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "T002", "만료된 토큰 오류입니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "T003", "지원되지 않는 토큰 오류입니다."),
    TOKEN_EMPTY_CLAIMS_STRING(HttpStatus.UNAUTHORIZED, "T004", "토큰 클레임 문자열이 비어 있음 오류입니다."),
    TOKEN_SUCCESS(HttpStatus.OK, "T005", "토큰 확인이 성공했습니다."),
    TOKEN_REISSUED(HttpStatus.CREATED, "T006", "토큰 재발급에 성공했습니다."),
    TOKEN_NOT_FOUND(HttpStatus.CREATED, "T007", "재 로그인이 필요합니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
