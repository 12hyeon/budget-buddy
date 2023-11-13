package hyeon.buddy.domain;

import hyeon.buddy.enums.RecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Table(name = "record")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    @JoinColumn(name = "category_id", nullable = false)
    private Long categoryId;

    private Long amount;

    private Integer percent;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private RecordType type;

    public static Record fromDay(Long total, Long uid, Long cid, LocalDate date, int percent){
        return Record.builder()
                .userId(uid)
                .categoryId(cid)
                .amount(total)
                .percent(percent)
                .date(date)
                .type(RecordType.DAY)
                .build();
    }

    public static Record fromMonth(Long total, Long uid, Long cid, LocalDate date, int percent){
        return Record.builder()
                .userId(uid)
                .categoryId(cid)
                .amount(total)
                .percent(percent)
                .date(date)
                .type(RecordType.MONTH)
                .build();
    }

    public void updateAmount(Long amount) { // int percent
        this.amount += amount;
        if (amount > 0) {
            this.percent += 1; // 지출한 날짜 횟수 기록
        }
    }

}
