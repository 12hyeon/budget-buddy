package hyeon.buddy.service;

import hyeon.buddy.dto.BudgetResponseDTO;
import hyeon.buddy.dto.BudgetSaveRequestDTO;
import hyeon.buddy.dto.BudgetUpdateRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface BudgetService {

    ExceptionResponse saveBudget(UserDetails userDetails, BudgetSaveRequestDTO dto);
    BudgetResponseDTO findBudget(UserDetails userDetails, boolean ascend,
                                 int minAmount, int maxAmount, String startDate, String endDate);
    ExceptionResponse updateBudget(UserDetails userDetails, Long bid, BudgetUpdateRequestDTO dto);
    ExceptionResponse deleteBudget(UserDetails userDetails, Long bid);

}
