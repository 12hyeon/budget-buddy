package hyeon.buddy.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUpdateRequestDTO {

    @Size(min = 30000, max = 2000000000, message = "예산으로 3만원 ~ 20억 사이의 금액을 입력해주세요.")
    private int amount;

}
