package hyeon.buddy.service;

import hyeon.buddy.domain.Budget;
import hyeon.buddy.domain.Category;
import hyeon.buddy.domain.User;
import hyeon.buddy.dto.BudgetDTO;
import hyeon.buddy.dto.BudgetResponseDTO;
import hyeon.buddy.dto.BudgetSaveRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.BudgetRepository;
import hyeon.buddy.repository.CategoryRepository;
import hyeon.buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 새로운 예산 저장
     *
     * @param userDetails 유저 정보
     * @param dto 생성할 예산 정보
     * @return 예산 저장 코드
     */
    @Transactional
    @Override
    public ExceptionResponse saveBudget(UserDetails userDetails,
                                        BudgetSaveRequestDTO dto) {
        // 예산 중복 확인
        budgetRepository.findByUserIdAndCategoryId(
                Long.parseLong(userDetails.getUsername()), dto.getCategory())
                .ifPresent(user -> {
                    throw new CustomException(ExceptionCode.BUDGET_EXISTING);
                });

        // date 형식 확인
        /*if (!Budget.checkDate(dto.getDate()) ) {
            throw new CustomException(ExceptionCode.BUDGET_INVALID);
        }*/

        User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new CustomException(ExceptionCode.CATEGORY_NOT_FOUND));

        budgetRepository.save(Budget.from(dto, user, category));

        return new ExceptionResponse(ExceptionCode.BUDGET_CREATED);
    }

    /**
     * 시작일, 종료일, 최소, 최대 금액, 금액순 선택에 따른 예산 조회
     *
     * @param userDetails 유저 정보
     * @param ascend      금액 오름차순
     * @param minAmount   최소 금액
     * @param maxAmount   최대 금액
     * @param startDate   시작일
     * @param endDate     종료일
     * @return 예산 조회 코드
     */
    @Transactional
    @Override //
    public BudgetResponseDTO findBudget(UserDetails userDetails, boolean ascend,
                                        int minAmount, int maxAmount, int startDate, int endDate) {

        // date 형식 확인
        /*if (!(Budget.checkDate(startDate) || Budget.checkDate(endDate)) {
            throw new CustomException(ExceptionCode.BUDGET_INVALID);
        }*/

        List<Budget> budgets;
        if (ascend) {
            budgets = budgetRepository
                    .findByUserIdAndDateBetweenAndAmountBetweenOrderByDateAsc(
                            Long.valueOf(userDetails.getUsername()),
                            minAmount, maxAmount, startDate, endDate);
        } else {
            budgets = budgetRepository
                    .findByUserIdAndDateBetweenAndAmountBetweenOrderByDateDesc(
                            Long.valueOf(userDetails.getUsername()),
                            minAmount, maxAmount, startDate, endDate);
        }

        List<BudgetDTO> budgetDTOList = budgets.stream().map(b -> new BudgetDTO(
                b.getId(), b.getDate(), b.getCategory().getTitle(), b.getAmount())).toList();

        return new BudgetResponseDTO(ExceptionCode.BUDGET_FOUND_OK, budgetDTOList);
    }

    /**
     * 예산 수정 (이번 달 이후)
     *
     * @param userDetails 유저 정보
     * @param bid 예산 id
     * @param dto 카테고리 id
     * @return 예산 수정 코드
     */
    @Transactional
    @Override
    public ExceptionResponse updateBudget(UserDetails userDetails, Long bid,
                                          BudgetSaveRequestDTO dto) {

        Budget budget = budgetRepository.findById(bid)
                .map(b -> {
                    if (!b.getUser().getId().equals(Long.parseLong(userDetails.getUsername()))) {
                        throw new CustomException(ExceptionCode.BUDGET_INVALID);
                    }
                    return b;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.BUDGET_NOT_FOUND));


        if (budget.getDate() > getNowDate()) {
            budget.updateBudget(dto.getAmount());
            return new ExceptionResponse(ExceptionCode.BUDGET_UPDATED);
        }

        // 다음 달 이전을 수정하는 경우, 미반영
        return new ExceptionResponse(ExceptionCode.BUDGET_IMMUTABLE);

    }

    /**
     * 예산 삭제 (이전 달 이후)
     *
     * @param userDetails 유저 정보
     * @param bid 예산 id
     * @return 예산 삭제 코드
     */
    @Transactional
    @Override
    public ExceptionResponse deleteBudget(UserDetails userDetails, Long bid) {

        Budget budget = budgetRepository.findById(bid)
                .map(b -> {
                    if (!b.getUser().getId().equals(Long.parseLong(userDetails.getUsername()))) {
                        throw new CustomException(ExceptionCode.BUDGET_INVALID);
                    }
                    return b;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.BUDGET_NOT_FOUND));


        if (budget.getDate() > getNowDate() - 1) {
            budgetRepository.delete(budget);
            return new ExceptionResponse(ExceptionCode.BUDGET_UPDATED);
        }

        // 다음 달 이전을 수정하는 경우, 미반영
        return new ExceptionResponse(ExceptionCode.BUDGET_IMMUTABLE);
    }

    /* 현재 년월을 int 타입으로 변경 */
    private int getNowDate() {

        LocalDate currentDate = LocalDate.now();
        YearMonth yearMonth = YearMonth.from(currentDate);

        return yearMonth.getYear() * 100 + yearMonth.getMonthValue();
    }


}