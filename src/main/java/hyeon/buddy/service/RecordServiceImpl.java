package hyeon.buddy.service;

import hyeon.buddy.domain.Category;
import hyeon.buddy.domain.Record;
import hyeon.buddy.dto.FeedbackDTO;
import hyeon.buddy.enums.RecordType;
import hyeon.buddy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordServiceImpl implements RecordService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RecordRepository recordRepository;
    private final BudgetRepository budgetRepository;

    /* 자정에 지출 합산 기록 및 피드백 전송 */
    @Transactional
    @Override
    public void saveDayAndMonthRecord() {

        saveDayRecord();
        log.info("하루 지출 내역 저장 완료: " + LocalDateTime.now());

        saveMonthRecord(); // 하루의 누적 데이터 이용
        log.info("한달 지출 내역 저장 완료: " + LocalDateTime.now());
    }

    /* 어제 하룯 동안 지출 기록 */
    private void saveDayRecord() {
        List<Category> categories = categoryRepository.findAll();
        LocalDate date = LocalDate.now().minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = YearMonth.from(date).format(formatter);

        userRepository.findAllUserIds().forEach(uid ->
        {
            Long total = expenseRepository.sumAmountByUserIdAndDate(uid, date);

            // 피드백
            log.info("userId :" + uid + ", total : " + total);
            List<FeedbackDTO> feedbackDTOS = new ArrayList<>();

            for (Category c : categories) {
                Long cid = c.getId();

                Long amount = expenseRepository.sumAmountByUserIdAndCategoryIdAndDate(
                        uid, cid, date);// 어제 하루간 카테고리별로 지출 합산

                Long budget = budgetRepository.findAmountByUserIdAndCategoryIdAndDate(
                        uid, cid,yearMonth);

                int percent = 0;
                if (amount > 0) { // 나누기 오류 처리
                    percent = (int) ((amount * 100) / total);
                }

                // 어제 하루동안 지출 내역 저장
                recordRepository.save(Record.fromDay(amount, uid, cid, date, percent));

                // 피드백 전송 log
                feedbackDTOS.add(new FeedbackDTO(date, c.getTitle(), amount, budget));
                log.info("Expense Details: Date={}, Category={}, Amount={}, Budget={}",
                        date, c.getTitle(), amount, budget);
            }

            // 피드백 전송
            //log.info("userId :" + uid + ", total : " + total);
            // new FeedbackResponseDTO(ExceptionCode.FEEDBACK_SENDER, total, feedbackDTOS);

        });
    }

    /* 이번 달 데이터가 누적 */
    private void saveMonthRecord() {

        List<Long> cIds = categoryRepository.findAllCategoryIds();
        LocalDate date = LocalDate.now().minusDays(1);

        userRepository.findAllUserIds().forEach(uid ->
        {
            for(Long cid : cIds) {

                // 기존 데이터 조회
                Optional<Record> monthRecord = recordRepository.findMonthRecords(
                        date, RecordType.MONTH, uid, cid);
                Optional<Record> dayRecord = recordRepository.findDayRecords(
                        date, RecordType.DAY,uid, cid);


                // 1일인 경우 or 해당 달에 데이터가 없는 경우, 누적될 한달 지출 내역 저장
                if (date.getDayOfYear() == 1 || monthRecord.isEmpty()) {
                    Record day = dayRecord.get();

                    recordRepository.save(Record.fromMonth(
                            day.getAmount(), uid, cid, date, day.getPercent()));

                } else {
                    Record month = monthRecord.get();
                    Record day = dayRecord.get();

                    // 어제 지출 누적
                    month.updateAmount(day.getAmount());

                }
            }
        });
    }

}