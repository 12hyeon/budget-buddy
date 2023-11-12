package hyeon.buddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoryRequestDTO {

    @NotBlank(message = "카테고리 명을 입력해 주세요.")
    @Size(min = 2, max = 6, message = "카테고리 명은 2~6자 사이로 입력해 주세요.")
    private String title;

}
