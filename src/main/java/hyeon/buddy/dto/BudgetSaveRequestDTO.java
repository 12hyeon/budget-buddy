package hyeon.buddy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSaveRequestDTO {

    @Size(min = 30000, max = 2000000000, message = "예산으로 3만원 ~ 20억 사이의 금액을 입력해주세요.")
    private int amount;

    @Size(min = 1, max = 10)
    @NotBlank(message = "해당하는 카테고리를 입력해 주세요. (1~10)")
    private Long category;

    @DateTimeFormat(pattern = "yyyyMM")
    @Size(min = 202311, max = 203012, message = "202301 형식으로 날짜를 입력해주세요.")
    private int date;

}
