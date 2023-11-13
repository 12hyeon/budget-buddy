package hyeon.buddy.repository;

import hyeon.buddy.domain.Budget;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    Optional<Budget> findByUserIdAndCategoryIdAndDate(Long uid, Long cid, String yearMonth);

    List<Budget> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
            Long userId, String startDate, String endDate, int minAmount, int maxAmount,
            PageRequest pageRequest);

    List<Budget> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
            Long userId, String startDate, String endDate, int minAmount, int maxAmount,
            PageRequest pageRequest);
}
