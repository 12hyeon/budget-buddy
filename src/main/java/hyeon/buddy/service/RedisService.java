package hyeon.buddy.service;

import hyeon.buddy.domain.Record;
import hyeon.buddy.domain.*;
import hyeon.buddy.dto.*;
import hyeon.buddy.enums.RecordType;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.RedisCustomException;
import hyeon.buddy.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final StatisticsRepository statisticsRepository;

    private final RedisService redisService;
    private final WebHookService webHookService;

    /* 자정에 지출 합산 기록 */
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

            // List<FeedbackDTO> feedbackDTOS = new ArrayList<>();

            for (Category c : categories) {
                Long cid = c.getId();

                Long amount = expenseRepository.sumAmountByUserIdAndCategoryIdAndDate(
                        uid, cid, date);// 어제 하루간 카테고리별로 지출 합산

                Long budget = budgetRepository.findAmountByUserIdAndCategoryIdAndDate(
                        uid, cid, yearMonth);

                int percent = 0;
                if (amount > 0) { // 나누기 오류 처리
                    percent = (int) ((amount * 100) / total);
                }

                // 어제 하루동안 지출 내역 저장
                recordRepository.save(Record.fromDay(amount, uid, cid, date, percent));

                // feedbackDTOS.add(new FeedbackDTO(date, c.getTitle(), amount, budget));
            }

            // 당일 지출 내용 전송
            // new FeedbackResponseDTO(ExceptionCode.FEEDBACK_SENDER, total, feedbackDTOS));

        });
    }

    /* 이번 달 데이터가 누적 */
    private void saveMonthRecord() {

        List<Long> cIds = categoryRepository.findAllCategoryIds();
        LocalDate date = LocalDate.now().minusDays(1);

        // 전날이 달의 마지막 날인 경우
        boolean lastDay = date.getDayOfMonth() == date.lengthOfMonth();
        HashMap<Long, RecordDTO> statist = new HashMap<>();

        userRepository.findAllUserIds().forEach(uid ->
        {
            for (Long cid : cIds) {

                // 기존 데이터 조회
                Optional<Record> monthRecord = recordRepository.findMonthRecords(
                        date, RecordType.MONTH, uid, cid);
                Optional<Record> dayRecord = recordRepository.findDayRecords(
                        date, RecordType.DAY, uid, cid);


                if (dayRecord.isPresent()) {
                    // 1일인 경우 or 해당 달에 데이터가 없는 경우, 누적될 한달 지출 내역 저장
                    if (date.getYear() == 1 || monthRecord.isEmpty()) {
                        Record day = dayRecord.get();

                        recordRepository.save(Record.fromMonth(
                                day.getAmount(), uid, cid, date, 1));

                    } else {
                        Record month = monthRecord.get();
                        Record day = dayRecord.get();

                        // 어제 지출 누적
                        month.updateAmount(day.getAmount());
                    }
                }

                if (lastDay && dayRecord.isPresent()) {
                    if (statist.isEmpty()) {
                        statist.put(cid, new RecordDTO(dayRecord.get().getAmount(), 1L));
                    } else {
                        statist.get(cid).update(dayRecord.get().getAmount());
                    }
                }
            }

        });

        if (!statist.isEmpty()) { // 한달간 총 사용 금액 및 사용자 명수 기록
            for (Long cid : statist.keySet()) {
                RecordDTO st = statist.get(cid);
                statisticsRepository.save(Statistics.from(cid, st.getAmount(), st.getCount(), date));
            }
        }
    }


    /* 당일 예산 추천 */
    @Transactional
    @Override
    public Object recommendToday(UserDetails userDetails) {

        User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        Long uid = user.getId();

        // 당일에 조회한 기록 확인
        String result;
        result = redisService.getRecommendInfo(uid);

        if (result != null) {
            webHookService.callFeedbackEvent(result);
            return new RecommendDayResponseDTO(ExceptionCode.RECOMMEND_SENDER_DAY, result);
        }

        // 이번 달을 String 형태로
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String yearMonth = YearMonth.from(date).format(formatter);

        List<Category> ctgs = categoryRepository.findAll();
        int standard = 1000;

        List<RecommendDayDTO> recommendDayDTOS = new ArrayList<>();
        for (Category c : ctgs) {
            Long cid = c.getId();

            // 일주일 간 평균 사용량 계산
            LocalDate sevenDays = LocalDate.now().minusDays(7 + 1);
            LocalDate endDay = LocalDate.now().minusDays(1);

            int total = 0;
            int count = 0;
            while (sevenDays.isBefore(endDay)) {
                Long amount = expenseRepository.sumAmountByUserIdAndCategoryIdAndDate(uid, cid, sevenDays);

                if (amount > 0) {
                    total += amount;
                    count += 1;
                }
                sevenDays = sevenDays.plusDays(1);
            }

            int avg = (count == 0) ? 0 : total / count;

            //log.info("Category ID: {} Total: {}, Count: {}, Average: {}", cid, total, count, avg);

            Optional<Budget> mBudget = budgetRepository.findByUserIdAndCategoryIdAndDate(uid, cid, yearMonth);
            if (mBudget.isPresent()) {

                // log.info("Month Budget: {}", mBudget.get().getAmount());

                // 이번 달 예산 및 사용한 예산
                int monthBudget = mBudget.get().getAmount();
                int usedBudget = Math.toIntExact(recordRepository.findMonthRecords(
                                date, RecordType.MONTH, uid, cid)
                        .map(Record::getAmount).orElse(0L));

                int restBudget = monthBudget - usedBudget;
                int restDay = YearMonth.now().lengthOfMonth() - date.getYear();
                int dayBudget = restBudget / restDay;

                /*log.info("Used Budget: {}, Remaining Budget: {}, Remaining Days: {}, Day Budget: {}",
                        usedBudget, restBudget, restDay, dayBudget);*/

                if (dayBudget > standard) { // 조건 1 : 남은 예산을 나머지 날짜로 분배해서 사용
                    recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), standard, monthBudget));
                } else if (total == 0 || avg <= standard) {  // 조건 2 : 일주일동안 사영한 날의 평균적인 지출을 추천
                    recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), standard, monthBudget));
                } else { // 조건 3 : 1000원 이하인 경우에는 1000원 추천
                    recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), avg, standard));
                }
            } else {// 조건 4 : 해당 타케고리의 예산이 없는 경우, 10000원
                recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), avg, 10 * standard));
            }
        }

        String s = webHookService.callFeedbackEvent(user.getAccount(), recommendDayDTOS);
        RecommendDayResponseDTO response = new RecommendDayResponseDTO(ExceptionCode.RECOMMEND_SENDER_DAY, s);

        // redis에 기록 및 예산 추천
        try {
            redisService.saveRecommendInfo(uid, s);
        } catch (Exception e) {
            log.error("Redis 연결 실패에 따른 userId(" + user.getId() + ") 예산 추천 정보 저장 오류 ");
            throw new RedisCustomException(ExceptionCode.RECOMMEND_NOT_CREATED, recommendDayDTOS);
        }

        return response;

    }

    @Override
    public void recommendTodayAll() {
        List<User> users = userRepository.findAll();

        for(User user : users) {
            Long uid = user.getId();

            // 당일에 조회한 기록 확인
            String result;
            result = redisService.getRecommendInfo(uid);

            if (result != null) {
                webHookService.callFeedbackEvent(result);
            }

            // 이번 달을 String 형태로
            LocalDate date = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
            String yearMonth = YearMonth.from(date).format(formatter);

            List<Category> ctgs = categoryRepository.findAll();
            int standard = 1000;

            List<RecommendDayDTO> recommendDayDTOS = new ArrayList<>();
            for (Category c : ctgs) {
                Long cid = c.getId();

                // 일주일 간 평균 사용량 계산
                LocalDate sevenDays = LocalDate.now().minusDays(7 + 1);
                LocalDate endDay = LocalDate.now().minusDays(1);

                int total = 0;
                int count = 0;
                while (sevenDays.isBefore(endDay)) {
                    Long amount = expenseRepository.sumAmountByUserIdAndCategoryIdAndDate(uid, cid, sevenDays);

                    if (amount > 0) {
                        total += amount;
                        count += 1;
                    }
                    sevenDays = sevenDays.plusDays(1);
                }

                int avg = (count == 0) ? 0 : total / count;

                Optional<Budget> mBudget = budgetRepository.findByUserIdAndCategoryIdAndDate(uid, cid, yearMonth);
                if (mBudget.isPresent()) {

                    // 이번 달 예산 및 사용한 예산
                    int monthBudget = mBudget.get().getAmount();
                    int usedBudget = Math.toIntExact(recordRepository.findMonthRecords(
                                    date, RecordType.MONTH, uid, cid)
                            .map(Record::getAmount).orElse(0L));

                    int restBudget = monthBudget - usedBudget;
                    int restDay = YearMonth.now().lengthOfMonth() - date.getYear();
                    int dayBudget = restBudget / restDay;

                    if (dayBudget > standard) { // 조건 1 : 남은 예산을 나머지 날짜로 분배해서 사용
                        recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), standard, monthBudget));
                    } else if (total == 0 || avg <= standard) {  // 조건 2 : 일주일동안 사영한 날의 평균적인 지출을 추천
                        recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), standard, monthBudget));
                    } else { // 조건 3 : 1000원 이하인 경우에는 1000원 추천
                        recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), avg, standard));
                    }
                } else {// 조건 4 : 해당 타케고리의 예산이 없는 경우, 10000원
                    recommendDayDTOS.add(new RecommendDayDTO(c.getTitle(), avg, 10 * standard));
                }
            }

            String s = webHookService.callFeedbackEvent(user.getAccount(), recommendDayDTOS);

            // redis에 기록 및 예산 추천
            try {
                redisService.saveRecommendInfo(uid, s);
            } catch (Exception e) {
                log.error("Redis 연결 실패에 따른 userId(" + user.getId() + ") 예산 추천 정보 저장 오류 ");
                throw new RedisCustomException(ExceptionCode.RECOMMEND_NOT_CREATED, recommendDayDTOS);
            }
        }

    }

    /* 이번 달의 카테고리별 예산 추천 */
    @Override
    public RecommendMonthResponseDTO recommendMonth(UserDetails userDetails) {
        User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));
        Long uid = user.getId();

        List<RecommendMonthDTO> recommendMonthDTOs = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll();

        // 날짜 기준 : 이전 달
        LocalDate date = LocalDate.now().minusMonths(1).withDayOfMonth(1);

        for (Category c : categories) { // 이전 기록에 따른 평균 계산
            Long cid = c.getId();

            List<Record> record = recordRepository.findRecordsForLastWeek(
                    date.minusDays(7), date, RecordType.MONTH, uid, cid);

            if (record.size() > 0) { // 조건 1: 기존 일주일 내역 기준 평균을 반영
                Long amount = 0L;
                for (Record r : record) {
                    amount += r.getAmount();
                }

                long avg = amount / record.size();
                recommendMonthDTOs.add(new RecommendMonthDTO(c.getTitle(), avg * date.getDayOfMonth()));
            }
            else {
                // 이전 달의 총 사용자의 평균
                Optional<Statistics> statistics = statisticsRepository.findStatistics(date, cid);

                if (statistics.isPresent()) {  // 조건 2: 총 사용자의 한달 지출 평균
                    Statistics s = statistics.get();
                    Long avgTotal = s.getAmount() / s.getCount();

                    recommendMonthDTOs.add(new RecommendMonthDTO(c.getTitle(), avgTotal));

                } else { // 조건 3 : 다른 사용자의 기록도 없으면, 임의로 30만원
                    recommendMonthDTOs.add(new RecommendMonthDTO(c.getTitle(), 300000L));
                }
            }
        }

        return new RecommendMonthResponseDTO(ExceptionCode.RECOMMEND_SENDER_MONTH, recommendMonthDTOs);

    }

    @Override
    public RecordResponseDTO findRecords(UserDetails userDetails, int page, boolean ascend, int minAmount,
                                         int maxAmount, LocalDate startDate, LocalDate endDate, Long cid) {

        int pageCount = 10;
        PageRequest pageRequest = PageRequest.of(page, pageCount, Sort.by("date"));

        List<Record> records;

        if (cid == null) { // 전체 카테코리 조회
            if (ascend) { // 오름차순
                records = recordRepository
                        .findByUserIdAndDateBetweenAndAmountBetweenAndTypeOrderByDateAsc(
                                Long.valueOf(userDetails.getUsername()),
                                startDate, endDate, minAmount, maxAmount,
                                RecordType.DAY, pageRequest);
            } else { // 내림 차순
                records = recordRepository
                        .findByUserIdAndDateBetweenAndAmountBetweenAndTypeOrderByDateDesc(
                                Long.valueOf(userDetails.getUsername()),
                                startDate, endDate, minAmount, maxAmount,
                                RecordType.DAY, pageRequest);
            }
        } else { // 카테고리별 조회

            Category category = categoryRepository.findById(cid)
                    .orElseThrow(() -> new CustomException(ExceptionCode.CATEGORY_NOT_FOUND));

            if (ascend) {
                records = recordRepository
                        .findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenAndTypeOrderByDateAsc(
                                Long.valueOf(userDetails.getUsername()), cid,
                                startDate, endDate, minAmount, maxAmount,
                                RecordType.DAY, pageRequest);
            } else {
                records = recordRepository
                        .findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenAndTypeOrderByDateDesc(
                                Long.valueOf(userDetails.getUsername()), cid,
                                startDate, endDate, minAmount, maxAmount,
                                RecordType.DAY, pageRequest);
            }
        }

        List<Category> ctg = categoryRepository.findAll();

        List<RecordsDTO> recordsDTOList = records.stream().map(r ->
                new RecordsDTO(
                        r.getId(), r.getDate(),
                        getCategoryNameByIdOrDefault(r.getCategoryId()),
                        r.getAmount(), r.getPercent()
                )
        ).toList();

        return new RecordResponseDTO(ExceptionCode.RECORD_FOUND_OK, recordsDTOList);
    }

    private String getCategoryNameByIdOrDefault(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(Category::getTitle)
                .orElse("?");
    }
}
