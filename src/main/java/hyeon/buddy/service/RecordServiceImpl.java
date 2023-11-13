package hyeon.buddy.service;

import hyeon.buddy.domain.Budget;
import hyeon.buddy.domain.Category;
import hyeon.buddy.domain.Record;
import hyeon.buddy.domain.User;
import hyeon.buddy.dto.FeedbackDTO;
import hyeon.buddy.dto.RecommendDTO;
import hyeon.buddy.dto.RecommendResponseDTO;
import hyeon.buddy.enums.RecordType;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final RedisService redisService;

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

            /*log.info("userId :" + uid + ", total : " + total);*/
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
                /*log.info("Expense Details: Date={}, Category={}, Amount={}, Budget={}",
                        date, c.getTitle(), amount, budget);*/
            }

            // 피드백 전송
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


                if (dayRecord.isPresent()) {
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
            }
        });
    }


    /* 당일 예산 추천 */
    @Transactional
    @Override
    public RecommendResponseDTO recommendToday(UserDetails userDetails) {


        User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        Long uid = user.getId();

        // 당일에 조회한 기록 확인
        try {
            RecommendResponseDTO result = redisService.getRecommendInfo(uid);
            if (result != null) {
                return result;
            }
        } catch (Exception e) {
            log.error("Redis 연결 실패에 따른 userId(" + user.getId() + ") 예산 추천 정보 저장 오류 : ", e);
        }

        // 이번 달을 String 형태로
        LocalDate date = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = YearMonth.from(date).format(formatter);

        List<Category> ctgs = categoryRepository.findAll();
        int standard = 1000;

        List<RecommendDTO> recommendDTOS = new ArrayList<>();
        for(Category c : ctgs) {
            Long cid = c.getId();

            Optional<Record> record = recordRepository.findDayRecords(date, RecordType.DAY, uid, cid);
            if (record.isPresent()) { // 이전 기록이 존재하는 경우

                // 일주일 간 평균 사용량 계산
                LocalDate sevenDays = LocalDate.now().minusDays(7 + 1);
                LocalDate endDay = LocalDate.now().minusDays(1);

                int total = 0;
                int count = 0;
                while (sevenDays.isBefore(endDay)) {
                    Long amount = expenseRepository
                            .sumAmountByUserIdAndCategoryIdAndDate(uid, cid, sevenDays);

                    if (amount > 0) {
                        total += amount;
                        count += 1;
                    }
                    sevenDays = sevenDays.plusDays(1);
                }

                int avg = total / count;

                Optional<Budget> mBudget = budgetRepository.findByUserIdAndCategoryIdAndDate(uid, cid, yearMonth);
                if (mBudget.isPresent()) {

                    // 이번 달 예산 및 사용한 예산
                    int monthBudget = mBudget.get().getAmount();
                    int usedBudget = Math.toIntExact(recordRepository.
                            findMonthRecords(date, RecordType.MONTH, uid, cid)
                            .map(Record::getAmount).orElse(0L));

                    int restBudget = monthBudget - usedBudget;
                    int restDay = YearMonth.now().lengthOfMonth() - date.getDayOfYear();
                    int dayBudget = monthBudget / restBudget;

                    if (dayBudget > standard) { // 조건 1: 남은 예산을 나머지 날짜로 분배해서 사용
                        recommendDTOS.add(new RecommendDTO(c.getTitle(), standard, monthBudget));
                    } else if (total == 0 || avg <= standard) {  // 조건 2 : 일주일동안 사영한 날의 평균적인 지출을 추천
                        recommendDTOS.add(new RecommendDTO(c.getTitle(), standard, monthBudget));
                    } else { // 조건 3 : 1000원 이하인 경우에는 1000원 추천
                        recommendDTOS.add(new RecommendDTO(c.getTitle(), avg, standard));
                    }
                }
            }
        }

        RecommendResponseDTO response = new RecommendResponseDTO(ExceptionCode.RECOMMEND_SENDER, recommendDTOS);

        // redis에 기록
        try {
            redisService.saveRecommendInfo(uid, response);
        } catch (Exception e) {
            log.error("Redis 연결 실패에 따른 userId(" + user.getId() + ") 예산 추천 정보 저장 오류 : ", e);
        }

        return response;
    }

}