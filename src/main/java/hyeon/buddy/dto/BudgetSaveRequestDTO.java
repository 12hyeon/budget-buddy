package hyeon.buddy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM", timezone = "Asia/Seoul")
    private String yearMonth;

}
