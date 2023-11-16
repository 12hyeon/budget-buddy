package hyeon.buddy.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

@Getter
public class RecommendDayResponseDTO extends ExceptionResponse {
    private String comment;

    public RecommendDayResponseDTO() {
        super(ExceptionCode.RECOMMEND_SENDER_DAY);

    }

    @JsonCreator
    public RecommendDayResponseDTO(RecommendDayResponseDTO dto) {
        super(ExceptionCode.RECOMMEND_SENDER_DAY);
        this.comment = dto.getComment();
    }

    public RecommendDayResponseDTO(ExceptionCode exceptionCode, String s) {
        super(exceptionCode);
        this.comment = s;
    }

}
