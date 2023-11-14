package hyeon.buddy.service;

import hyeon.buddy.dto.RecommendMonthResponseDTO;
import hyeon.buddy.dto.RecordResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

public interface RecordService {

    void saveDayAndMonthRecord();
    Object recommendToday(UserDetails userDetails);
    RecommendMonthResponseDTO recommendMonth(UserDetails userDetails);

    RecordResponseDTO findRecords(UserDetails userDetails, int page, boolean ascend,
                                  int minAmount, int maxAmount, LocalDate startDate,
                                  LocalDate endDate, Long cid);

}
