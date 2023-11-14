package hyeon.buddy.service;

import hyeon.buddy.dto.RecommendResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface RecordService {

    void saveDayAndMonthRecord();
    RecommendResponseDTO recommendToday(UserDetails userDetails);

}
