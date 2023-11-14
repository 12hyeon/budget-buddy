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

    // 카테고리
    CATEGORY_CREATED(HttpStatus.CREATED, "C000","카테고리를 생성했습니다."),
    CATEGORY_DUPLICATED_TITLE(HttpStatus.CONFLICT, "C001","유사한 카테고리가 존재합니다."),
    CATEGORY_EXCEEDED_COUNT(HttpStatus.CONFLICT, "C002","카테고리 개수가 10개를 초과합니다."),
    CATEGORY_FOUND_OK(HttpStatus.OK, "C003","카테고리 조회를 성공했습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C004","카테고리를 찾을 수 없습니다."),

    // 예산
    BUDGET_CREATED(HttpStatus.CREATED, "B000", "예산 생성을 성공하였습니다."),
    BUDGET_EXISTING(HttpStatus.OK, "B001", "존재하는 예산이 있습니다."),
    BUDGET_IMMUTABLE(HttpStatus.CONFLICT, "B002", "변경 불가능한 예산입니다."),
    BUDGET_UPDATED(HttpStatus.OK, "B003", "예산 수정을 완료하였습니다."),
    BUDGET_FOUND_OK(HttpStatus.OK, "B004","예산 조회를 성공했습니다."),
    BUDGET_INVALID(HttpStatus.BAD_REQUEST, "B005","잘못된 예산 접근입니다."),
    BUDGET_NOT_FOUND(HttpStatus.NOT_FOUND, "B006","예산을 찾을 수 없습니다."),
    BUDGET_DELETED(HttpStatus.OK, "B007","기존 예산을 삭제합니다."),

    // 지출
    EXPENSE_CREATED(HttpStatus.CREATED, "E000", "지출 저장을 성공하였습니다."),
    EXPENSE_EXISTING(HttpStatus.OK, "E001", "존재하는 중복된 지출이 있습니다."),
    EXPENSE_IMMUTABLE(HttpStatus.CONFLICT, "E002", "변경 불가능한 지출입니다."),
    EXPENSE_UPDATED(HttpStatus.OK, "E003", "지출 수정을 완료하였습니다."),
    EXPENSE_FOUND_OK(HttpStatus.OK, "E004", "지출 조회를 성공했습니다."),
    EXPENSE_INVALID(HttpStatus.BAD_REQUEST, "E005", "잘못된 지출 접근입니다."),
    EXPENSE_NOT_FOUND(HttpStatus.NOT_FOUND, "E006", "지출을 찾을 수 없습니다."),
    EXPENSE_DELETED(HttpStatus.OK, "E007", "기존 지출을 삭제합니다."),

    // 피드백
    FEEDBACK_SENDER(HttpStatus.OK, "F001", "피드백을 전송하였습니다."),

    RECOMMEND_SENDER(HttpStatus.OK, "F001", "당일 카테고리별 예산 추천을 전송하였습니다."),

    // redis
    RECOMMEND_NOT_CREATED(HttpStatus.CREATED, "R000", "추천 정보 저장을 실패하였습니다."),
    RECOMMEND_NOT_FOUND(HttpStatus.NOT_ACCEPTABLE, "R001", "추천 정보 조회에 실패하였습니다."),

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
