package hyeon.buddy.repository;

import hyeon.buddy.domain.Expense;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
            Long userId, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);

    List<Expense> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
            Long userId, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);


    List<Expense> findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
            Long userId, Long categoryId,
            LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);

    List<Expense> findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
            Long userId, Long categoryId,
            LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);
}