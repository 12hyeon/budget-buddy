package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class RecordResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<RecordsDTO> records;

    public RecordResponseDTO(ExceptionCode exceptionCode, List<RecordsDTO> records) {
        super(exceptionCode);
        this.count = records.size();
        this.records = records;
    }

}
