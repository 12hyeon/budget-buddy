package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BudgetDTO { // 사용자에게 필요한 예산 정보

    private long id;
    private int date;
    private String category;
    private int amount;

}
