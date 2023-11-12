package hyeon.buddy.service;

import hyeon.buddy.dto.BudgetResponseDTO;
import hyeon.buddy.dto.BudgetSaveRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.YearMonth;

public interface BudgetService {

    ExceptionResponse saveBudget(UserDetails userDetails, BudgetSaveRequestDTO dto);
    BudgetResponseDTO findBudget(UserDetails userDetails, boolean ascend,
                                 int minAmount, int maxAmount, YearMonth startDate, YearMonth endDate);
    ExceptionResponse updateBudget(UserDetails userDetails, Long bid, BudgetSaveRequestDTO dto);
    ExceptionResponse deleteBudget(UserDetails userDetails, Long bid);

}
