package hyeon.buddy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class RecordsDTO {

    private Long id;
    private LocalDate date;
    private String category;
    private Long amount;
    private Integer percent;

}
