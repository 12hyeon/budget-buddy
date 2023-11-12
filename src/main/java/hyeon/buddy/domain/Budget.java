package hyeon.buddy.domain;

import hyeon.buddy.dto.BudgetSaveRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

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

    private YearMonth date;


    public static Budget from(BudgetSaveRequestDTO dto, User user, Category category){
        return Budget.builder()
                .user(user)
                .category(category)
                .amount(dto.getAmount())
                .date(dto.getDate())
                .build();

    }

    /*public static boolean checkDate(int date) {

        int year = date%100; // 2023~2030
        int month = date/100; // 1~12

        return !(2023 > year || year > 2023 || month < 1 || month > 13);
    }*/

    public void updateBudget(int amount) {
        this.amount = amount;
    }
}

