package hyeon.buddy.service;

import hyeon.buddy.domain.Category;
import hyeon.buddy.domain.Expense;
import hyeon.buddy.domain.User;
import hyeon.buddy.dto.ExpenseDTO;
import hyeon.buddy.dto.ExpenseResponseDTO;
import hyeon.buddy.dto.ExpenseSaveRequestDTO;
import hyeon.buddy.dto.ExpenseUpdateRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.CategoryRepository;
import hyeon.buddy.repository.ExpenseRepository;
import hyeon.buddy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 새로운 지출 저장
     *
     * @param userDetails 유저 정보
     * @param dto 생성할 지출 정보
     * @return 지출 저장 코드
     */
    @Transactional
    @Override
    public ExceptionResponse saveExpense(UserDetails userDetails,
                                        ExpenseSaveRequestDTO dto) {

        User user = userRepository.findById(Long.valueOf(userDetails.getUsername()))
                .orElseThrow(() -> new CustomException(ExceptionCode.USER_NOT_FOUND));

        Category category = categoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new CustomException(ExceptionCode.CATEGORY_NOT_FOUND));

        expenseRepository.save(Expense.from(dto, user, category));

        return new ExceptionResponse(ExceptionCode.EXPENSE_CREATED);
    }


    /**
     * 시작일, 종료일, 최소, 최대 금액, 금액순, 카테고리 선택에 따른 지출 조회
     *
     * @param userDetails 유저 정보
     * @param ascend      금액 오름차순
     * @param minAmount   최소 금액
     * @param maxAmount   최대 금액
     * @param startDate   시작일
     * @param endDate     종료일
     * @param cid         카테고리 id
     * @return 지출 조회 코드
     */
    @Transactional
    @Override
    public ExpenseResponseDTO findExpense(UserDetails userDetails, int page, boolean ascend, int minAmount,
                                          int maxAmount, LocalDate startDate, LocalDate endDate, Long cid) {

        int pageCount = 10;
        PageRequest pageRequest = PageRequest.of(page, pageCount, Sort.by("amount"));

        List<Expense> expenses;

        if (cid == null) { // 전체 카테코리 조회
            if (ascend) { // 오름차순
                expenses = expenseRepository
                        .findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
                                Long.valueOf(userDetails.getUsername()),
                                startDate, endDate, minAmount, maxAmount, pageRequest);
            } else { // 내림 차순
                expenses = expenseRepository
                        .findByUserIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
                                Long.valueOf(userDetails.getUsername()),
                                startDate, endDate, minAmount, maxAmount, pageRequest);
            }
        } else { // 카테고리별 조회

            Category category = categoryRepository.findById(cid)
                    .orElseThrow(() -> new CustomException(ExceptionCode.CATEGORY_NOT_FOUND));

            if (ascend) {
                expenses = expenseRepository
                        .findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountAsc(
                                Long.valueOf(userDetails.getUsername()), cid,
                                startDate, endDate, minAmount, maxAmount, pageRequest);
            } else {
                expenses = expenseRepository
                        .findByUserIdAndCategoryIdAndDateBetweenAndAmountBetweenOrderByAmountDesc(
                                Long.valueOf(userDetails.getUsername()), cid,
                                startDate, endDate, minAmount, maxAmount, pageRequest);
            }
        }

        List<ExpenseDTO> expenseDTOList = expenses.stream().map(e -> new ExpenseDTO(
                e.getId(), e.getDate(),
                e.getCategory().getTitle(), e.getAmount(),
                e.getDetail(), e.getIsException())).toList();

        return new ExpenseResponseDTO(ExceptionCode.EXPENSE_FOUND_OK, expenseDTOList);
    }

    /**
     * 지출 수정 (오늘)
     *
     * @param userDetails 유저 정보
     * @param eid 지출 id
     * @param dto 카테고리 id
     * @return 지출 수정 코드
     */
    @Transactional
    @Override
    public ExceptionResponse updateExpense(UserDetails userDetails, Long eid,
                                          ExpenseUpdateRequestDTO dto) {

        Expense expense = expenseRepository.findById(eid)
                .map(b -> {
                    if (!b.getUser().getId().equals(Long.parseLong(userDetails.getUsername()))) {
                        throw new CustomException(ExceptionCode.EXPENSE_INVALID);
                    }
                    return b;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.EXPENSE_NOT_FOUND));

        LocalDate date = expense.getDate();

        // 당일만 수정
        if (date.equals(LocalDate.now())) {
            expense.updateExpense(dto);
            return new ExceptionResponse(ExceptionCode.EXPENSE_UPDATED);
        }

        // 당일 외 수정하는 경우, 미반영
        return new ExceptionResponse(ExceptionCode.EXPENSE_IMMUTABLE);

    }

    /**
     * 지출 삭제 (오늘)
     *
     * @param userDetails 유저 정보
     * @param eid 지출 id
     * @return 지출 삭제 코드
     */
    @Transactional
    @Override
    public ExceptionResponse deleteExpense(UserDetails userDetails, Long eid) {

        Expense expense = expenseRepository.findById(eid)
                .map(b -> {
                    if (!b.getUser().getId().equals(Long.parseLong(userDetails.getUsername()))) {
                        throw new CustomException(ExceptionCode.EXPENSE_INVALID);
                    }
                    return b;
                })
                .orElseThrow(() -> new CustomException(ExceptionCode.EXPENSE_NOT_FOUND));

        LocalDate date = expense.getDate();

        // 오늘만 수정 가능
        if (date.equals(LocalDate.now())) {
            expenseRepository.delete(expense);
            return new ExceptionResponse(ExceptionCode.EXPENSE_DELETED);
        }

        // 당일 외 수정하는 경우, 미반영
        return new ExceptionResponse(ExceptionCode.EXPENSE_IMMUTABLE);

    }

    /* 전날 이용 기록 사용자에게 전달 => 비동기 */
    @Override
    public void sendFeedback() {

    }

}