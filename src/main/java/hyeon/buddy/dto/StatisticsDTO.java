package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatisticsDTO {

    private Long amount;
    private Long count;

    public void update(Long amount) {
        this.count += 1L;
        this.amount += amount;
    }

}
