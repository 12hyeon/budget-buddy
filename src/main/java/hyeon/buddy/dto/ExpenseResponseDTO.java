package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ExpenseResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<ExpenseDTO> expenses;

    public ExpenseResponseDTO(ExceptionCode exceptionCode, List<ExpenseDTO> expenses) {
        super(exceptionCode);
        this.count = expenses.size();
        this.expenses = expenses;
    }

}
