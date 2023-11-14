package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class FeedbackDTO { // 사용자에게 필요한 어제 지출 정보

    private LocalDate date;
    private String category;
    private Long amount;
    private Long budget;

}
