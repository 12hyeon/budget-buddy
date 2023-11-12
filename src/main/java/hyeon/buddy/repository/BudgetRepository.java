package hyeon.buddy.repository;

import hyeon.buddy.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryId(Long uid, Long cid);

    List<Budget> findByUserIdAndDateBetweenAndAmountBetweenOrderByDateAsc(
            Long userId, int startDate, int endDate, int minAmount, int maxAmount);

    List<Budget> findByUserIdAndDateBetweenAndAmountBetweenOrderByDateDesc(
            Long userId, int startDate, int endDate, int minAmount, int maxAmount);
}