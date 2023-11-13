package hyeon.buddy.repository;

import hyeon.buddy.domain.Budget;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user.id = :uid AND e.category.id = :cid " +
            "AND e.date LIKE CONCAT(:yearMonth, '%')")
    Long findAmountByUserIdAndCategoryIdAndDate(
            @Param("uid") Long userId,
            @Param("cid") Long categoryId,
            @Param("yearMonth") String yearMonth);


    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :uid AND e.date LIKE CONCAT(:yearMonth, '%')")
    List<Budget> findByUserIdAndDate(
            @Param("uid") Long userId,
            @Param("yearMonth") String yearMonth);
}
