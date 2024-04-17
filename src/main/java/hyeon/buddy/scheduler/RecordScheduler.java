package hyeon.buddy.scheduler;

import hyeon.buddy.service.RecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class RecordScheduler {

    private final RecordService recordService;

    // @Scheduled(cron = "0 0/1 * * * *") // 매 1분마다 실행
    @Scheduled(cron = "0 0 0 * * *") // 매일 0시 0분 0초에 실행
    public void saveScheduledTag() {
        recordService.saveDayAndMonthRecord();
    }


    @Scheduled(cron = "8 0 0 * * *") // 매일 8시에 당일 예산 추천 정보 알림
    public void recommendDay() {
        recordService.recommendTodayAll();
    }

}
