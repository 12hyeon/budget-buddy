package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
@AllArgsConstructor
public class BudgetDTO { // 사용자에게 필요한 예산 정보

    private long id;
    private YearMonth date;
    private String category;
    private int amount;

}
