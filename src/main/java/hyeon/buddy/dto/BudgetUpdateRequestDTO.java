package hyeon.buddy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUpdateRequestDTO {

    @Min(value = 30000, message = "예산으로 3만원 이상을 입력해주세요.")
    @Max(value = 2000000000, message = "예산으로 20억 이하를 입력해주세요.")
    private int amount;

}
