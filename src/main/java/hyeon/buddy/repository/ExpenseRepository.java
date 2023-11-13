package hyeon.buddy.repository;

import hyeon.buddy.domain.Expense;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
            Long userId, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);

    List<Expense> findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
            Long userId, LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);

    // 카테고리 추가
    List<Expense> findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
            Long userId, Long categoryId,
            LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);

    List<Expense> findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
            Long userId, Long categoryId,
            LocalDate startDate, LocalDate endDate, int minAmount, int maxAmount,
            PageRequest pageable);


    /* 당일 지출 합산 */

    // 카테고리 모두
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :uid AND e.date = :date")
    Long sumAmountByUserIdAndDate(
            @Param("uid") Long userId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user.id = :uid AND e.category.id =:cid AND e.date = :date")
    Long sumAmountByUserIdAndCategoryIdAndDate(
            @Param("uid") Long userId,
            @Param("cid") Long categoryId,
            @Param("date") LocalDate date);


    /* 한달 지출 합산 : null 0으로 처리 */

    // 카테고리 모두
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user.id = :uid AND YEAR(e.date) = YEAR(:date) AND MONTH(e.date) = MONTH(:date)")
    Long sumAmountByUserIdAndMonth(
            @Param("uid") Long userId, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
            "WHERE e.user.id = :uid AND e.category.id = :cid AND YEAR(e.date) = YEAR(:date) AND MONTH(e.date) = MONTH(:date)")
    Long sumAmountByUserIdAndCategoryIdAndMonth(
            @Param("uid") Long userId,
            @Param("cid") Long categoryId,
            @Param("date") LocalDate date);

//
//    /* 한달 지출 합산 : null 0으로 처리 */
//
//    // 카테고리 모두
//    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
//            "WHERE e.user_id = :userId AND e.is_exception = false " +
//            "AND YEAR(e.date) = YEAR(:date) " +
//            "AND MONTH(e.date) = MONTH(:date)")
//    Long sumAmountByUserIdAndMonth(
//            @Param("userId") Long userId, @Param("date") LocalDate date);

//    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e " +
//            "WHERE e.user_id = :userId AND e.category_id = :categoryId " +
//            "AND e.is_exception = false " +
//            "AND YEAR(e.date) = YEAR(:date) " +
//            "AND MONTH(e.date) = MONTH(:date)")
//    Long sumAmountByUserIdAndCategoryIdAndMonth(
//            @Param("userId") Long userId,
//            @Param("categoryId") Long categoryId,
//            @Param("date") LocalDate date);



}