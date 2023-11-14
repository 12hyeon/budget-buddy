package hyeon.buddy.repository;

import hyeon.buddy.domain.Record;
import hyeon.buddy.enums.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM Record r " +
            "WHERE r.userId = :userId " +
            "AND r.date BETWEEN :startDate AND :endDate")
    Long sumAmountForCurrentMonth(@Param("userId") Long userId,
                                  @Param("startDate") LocalDate startDate,
                                  @Param("endDate") LocalDate endDate);

    @Query("SELECT r FROM Record r " +
            "WHERE FUNCTION('MONTH', r.date) = FUNCTION('MONTH', :currentDate) " +
            "AND r.type = :recordType " +
            "AND r.userId = :userId " +
            "AND r.categoryId = :categoryId")
    Optional<Record> findMonthRecords(@Param("currentDate") LocalDate currentDate,
                                      @Param("recordType") RecordType recordType,
                                      @Param("userId") Long userId,
                                      @Param("categoryId") Long categoryId);

    @Query("SELECT r FROM Record r " +
            "WHERE r.date = :currentDate " +
            "AND r.type = :recordType " +
            "AND r.userId = :userId " +
            "AND r.categoryId = :categoryId")
    Optional<Record> findDayRecords(@Param("currentDate") LocalDate currentDate,
                                @Param("recordType") RecordType recordType,
                                @Param("userId") Long userId,
                                @Param("categoryId") Long categoryId);

}