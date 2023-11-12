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


    // 비밀번호 관련 예외 코드
    INVALID_PASSWORD_USUAL_PASSWORD(HttpStatus.BAD_REQUEST, "P001", "자주 사용되는 비밀번호는 사용할 수 없습니다."),
    INVALID_PASSWORD_AT_LEAST_2_TYPES(HttpStatus.BAD_REQUEST, "P002", "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다."),
    INVALID_PASSWORD_SAME_CHARACTERS(HttpStatus.BAD_REQUEST, "P003", "같은 문자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다."),
    INVALID_PASSWORD_CONSECUTIVE_CHARACTERS(HttpStatus.BAD_REQUEST, "P004", "연속된 문자 또는 숫자가 3개 이상 포함된 비밀번호는 사용할 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
