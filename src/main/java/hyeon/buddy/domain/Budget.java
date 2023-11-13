package hyeon.buddy.domain;

import hyeon.buddy.dto.BudgetSaveRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Table(name = "budget")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private int amount;

    private String date;

    public static Budget from(BudgetSaveRequestDTO dto, User user, Category category){
        return Budget.builder()
                .user(user)
                .category(category)
                .amount(dto.getAmount())
                .date(dto.getYearMonth())
                .build();
    }

    public void updateBudget(int amount) {
        this.amount = amount;
    }
}

