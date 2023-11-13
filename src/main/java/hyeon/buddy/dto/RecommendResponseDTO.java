package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<RecommendDTO> budgets;

    public RecommendResponseDTO(ExceptionCode exceptionCode, List<RecommendDTO> budgets) {
        super(exceptionCode);
        this.count = budgets.size();
        this.budgets = budgets;
    }

}
