package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class BudgetResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<BudgetDTO> budgets;

    public BudgetResponseDTO(ExceptionCode exceptionCode, List<BudgetDTO> budgets) {
        super(exceptionCode);
        this.count = budgets.size();
        this.budgets = budgets;
    }

}
