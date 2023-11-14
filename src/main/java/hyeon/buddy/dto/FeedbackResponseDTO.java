package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class FeedbackResponseDTO extends ExceptionResponse {
    private final Long total;
    private final int count;
    private final List<FeedbackDTO> records;

    public FeedbackResponseDTO(ExceptionCode exceptionCode, Long total, List<FeedbackDTO> records) {
        super(exceptionCode);
        this.total = total;
        this.count = records.size();
        this.records = records;
    }

}
