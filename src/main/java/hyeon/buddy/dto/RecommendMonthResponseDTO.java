package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendMonthResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<RecommendMonthDTO> budgets;

    public RecommendMonthResponseDTO(ExceptionCode exceptionCode, List<RecommendMonthDTO> budgets) {
        super(exceptionCode);
        this.count = budgets.size();
        this.budgets = budgets;
    }

}
