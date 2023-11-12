package hyeon.buddy.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetSaveRequestDTO {

    @Min(value = 30000, message = "예산으로 3만원 이상을 입력해주세요.")
    @Max(value = 2000000000, message = "예산으로 20억 이하를 입력해주세요.")
    private int amount;

    @Min(value = 1, message = "카테고리를 1 이상 입력해주세요.")
    @Max(value = 10, message = "카테고리를 10 이하 입력해주세요.")
    @NotNull(message = "해당하는 카테고리를 입력해 주세요. (1~10)")
    private Long category;

    @NotBlank(message = "날짜를 입력해주세요.")
    @Pattern(regexp = "^(202[0-9]|2030)(0[1-9]|1[0-2])$", message = "202311 형식으로 날짜를 입력해주세요.")
    private YearMonth date;

}
