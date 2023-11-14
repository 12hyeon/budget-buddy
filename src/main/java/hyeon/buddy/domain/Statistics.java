package hyeon.buddy.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Builder
@Entity
@Table(name = "statistics")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Statistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "category_id", nullable = false)
    private Long categoryId;

    private Long amount;

    private Long count;

    private LocalDate date;

    public static Statistics from(Long cid, Long amount, Long count, LocalDate date){
        return Statistics.builder()
                .categoryId(cid)
                .amount(amount)
                .count(count)
                .date(date)
                .build();
    }
}
