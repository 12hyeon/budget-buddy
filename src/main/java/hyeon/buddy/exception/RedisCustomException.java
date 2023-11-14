package hyeon.buddy.exception;

import hyeon.buddy.dto.RecommendDayDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class RedisCustomException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    private final int count;
    private final List<RecommendDayDTO> budgets;

    public RedisCustomException(ExceptionCode exceptionCode, List<RecommendDayDTO> budgets) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
        this.count = budgets.size();
        this.budgets = budgets;
    }
}