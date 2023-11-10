package hyeon.buddy.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "statistics")
@Getter
public class Statistics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "last_month_spending")
    private Long lastMonthSpending;

    @Column(name = "current_month_spending")
    private Float currentMonthSpending;

    @Column(name = "last_week_spending")
    private Long lastWeekSpending;

    @Column(name = "current_week_spending")
    private Float currentWeekSpending;

    @Column(name = "other_avg_spending")
    private Long otherAvgSpending;
}
