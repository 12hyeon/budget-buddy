package hyeon.buddy.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RecordService {

    void saveDayAndMonthRecord();
    Object recommendToday(UserDetails userDetails);

}
