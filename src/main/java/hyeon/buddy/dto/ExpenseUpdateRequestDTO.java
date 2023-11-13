package hyeon.buddy.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseUpdateRequestDTO {

    @Max(value = 2000000000, message = "지출로 20억 이하를 입력해주세요.")
    private int amount;

    @Size(max = 30, message = "30자 이하로 설명을 입력해주세요.")
    private String detail;

    @Builder.Default
    private Boolean isException = false;

}
