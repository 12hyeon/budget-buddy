package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExpenseDTO { // 사용자에게 필요한 지출 정보

    private long id;
    private LocalDate date;
    private String category;
    private int amount;
    private String details;
    private boolean isException;

}
