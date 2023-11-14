package hyeon.buddy.domain;

import hyeon.buddy.dto.ExpenseSaveRequestDTO;
import hyeon.buddy.dto.ExpenseUpdateRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Entity
@Table(name = "expense")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    private Integer amount;

    private String detail;

    @Column(name = "is_exception")
    private Boolean isException;

    private LocalDate date;

    public static Expense from(ExpenseSaveRequestDTO dto, User user, Category category){
        return Expense.builder()
                .user(user)
                .category(category)
                .amount(dto.getAmount())
                .detail(dto.getDetail())
                .isException(dto.getIsException())
                .date(LocalDate.now())
                .build();
    }

    public void updateExpense(ExpenseUpdateRequestDTO dto) {
        this.amount = dto.getAmount();
        this.detail = dto.getDetail();
        this.isException = dto.getIsException();
    }
}
