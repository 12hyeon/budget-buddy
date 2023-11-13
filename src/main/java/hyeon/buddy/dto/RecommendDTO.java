package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RecommendDTO { // 사용자에게 필요한 당일 추천 예산 정보

    private String category;
    private int todayBudget;
    private int monthBudget;

}
