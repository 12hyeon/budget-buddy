package hyeon.buddy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendResponseDTO extends ExceptionResponse {
    private int count;
    private List<RecommendDTO> budgets;

    public RecommendResponseDTO() {
        super(ExceptionCode.RECOMMEND_SENDER);

    }

    @JsonCreator
    public RecommendResponseDTO(RecommendResponseDTO dto) {
        super(ExceptionCode.RECOMMEND_SENDER);
        this.count = dto.getCount();
        this.budgets = dto.getBudgets();
    }

    public RecommendResponseDTO(ExceptionCode exceptionCode, List<RecommendDTO> budgets) {
        super(exceptionCode);
        this.count = budgets.size();
        this.budgets = budgets;
    }

}
