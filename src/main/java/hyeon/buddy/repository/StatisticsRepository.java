package hyeon.buddy.repository;

import hyeon.buddy.domain.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    @Query("SELECT r FROM Record r " +
            "WHERE FUNCTION('MONTH', r.date) = FUNCTION('MONTH', :currentDate) " +
            "AND FUNCTION('YEAR', r.date) = FUNCTION('YEAR', :currentDate) " +
            "AND r.categoryId = :categoryId")
    Optional<Statistics> findStatistics(
            @Param("currentDate") LocalDate currentDate, @Param("categoryId") Long categoryId);
}