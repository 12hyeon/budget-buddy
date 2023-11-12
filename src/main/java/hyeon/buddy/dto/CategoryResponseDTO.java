package hyeon.buddy.dto;

import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class CategoryResponseDTO extends ExceptionResponse {
    private final int count;
    private final List<String> category;

    public CategoryResponseDTO(ExceptionCode exceptionCode, List<String> category) {
        super(exceptionCode);
        this.count = category.toArray().length;
        this.category = category;
    }

}
