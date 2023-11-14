package hyeon.buddy.service;

import hyeon.buddy.dto.ExpenseResponseDTO;
import hyeon.buddy.dto.ExpenseSaveRequestDTO;
import hyeon.buddy.dto.ExpenseUpdateRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;

public interface ExpenseService {

    ExceptionResponse saveExpense(UserDetails userDetails, ExpenseSaveRequestDTO dto);
    ExpenseResponseDTO findExpense(UserDetails userDetails, int page, boolean ascend,
                                   int minAmount, int maxAmount, LocalDate startDate,
                                   LocalDate endDate, Long cid);
    ExceptionResponse updateExpense(UserDetails userDetails, Long bid, ExpenseUpdateRequestDTO dto);
    ExceptionResponse deleteExpense(UserDetails userDetails, Long bid);

}
