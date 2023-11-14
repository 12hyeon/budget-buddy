package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecommendMonthDTO { // 사용자에게 필요한 월별 추천 예산 정보

    private String category;
    private Long monthBudget;
}
