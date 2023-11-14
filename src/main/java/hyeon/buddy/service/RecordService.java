package hyeon.buddy.service;

import hyeon.buddy.dto.RecommendMonthResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface RecordService {

    void saveDayAndMonthRecord();
    Object recommendToday(UserDetails userDetails);
    RecommendMonthResponseDTO recommendMonth(UserDetails userDetails);

}
