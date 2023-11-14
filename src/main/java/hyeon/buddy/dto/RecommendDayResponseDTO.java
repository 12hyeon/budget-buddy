package hyeon.buddy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class RecommendDayResponseDTO extends ExceptionResponse {
    private int count;
    private List<RecommendDayDTO> budgets;

    public RecommendDayResponseDTO() {
        super(ExceptionCode.RECOMMEND_SENDER_DAY);

    }

    @JsonCreator
    public RecommendDayResponseDTO(RecommendDayResponseDTO dto) {
        super(ExceptionCode.RECOMMEND_SENDER_DAY);
        this.count = dto.getCount();
        this.budgets = dto.getBudgets();
    }

    public RecommendDayResponseDTO(ExceptionCode exceptionCode, List<RecommendDayDTO> budgets) {
        super(exceptionCode);
        this.count = budgets.size();
        this.budgets = budgets;
    }

}
