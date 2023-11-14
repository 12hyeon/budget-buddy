package hyeon.buddy.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyeon.buddy.dto.ExpenseSaveRequestDTO;
import hyeon.buddy.dto.ExpenseUpdateRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Tag(name = "Expense API", description = "지출와 관련된 API")
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
@RestController
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    @Operation(summary = "지출 생성", description = "새로운 지출을 생성합니다.")
    public ResponseEntity<ExceptionResponse> saveExpense(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody @Valid ExpenseSaveRequestDTO dto) {
        return ResponseEntity.status(OK).body(expenseService.saveExpense(userDetails, dto));
    }

    @PutMapping("/{eid}")
    @Operation(summary = "지출 수정", description = "당일 지출을 수정합니다. ")
    public ResponseEntity<ExceptionResponse> updateExpense(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long eid,
                                                          @RequestBody @Valid ExpenseUpdateRequestDTO dto) {
        return ResponseEntity.status(OK).body(expenseService.updateExpense(userDetails, eid, dto));
    }

    @DeleteMapping("/{eid}")
    @Operation(summary = "지출 삭제", description = "당일 지출을 삭제합니다.")
    public ResponseEntity<ExceptionResponse> deleteExpense(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long eid) {
        return ResponseEntity.status(OK).body(expenseService.deleteExpense(userDetails, eid));
    }

    @GetMapping
    @Operation(summary = "지출 조회", description = "날짜, 카테고리, 비옹에 따라 기록한 지출을 조회합니다.")
    public ResponseEntity<ExceptionResponse> findExpense(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue =  "0") int page,
            @RequestParam(defaultValue = "true") Boolean ascend,
            @RequestParam(defaultValue = "0") int minAmount,
            @RequestParam(defaultValue = "2000000000") int maxAmount,
            @RequestParam(defaultValue = "2023-11-01", value = "startDate (2023-11-01)")
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(defaultValue = "2030-12-31", value = "startDate (2030-12-31)")
            @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false) Long category) {
        return ResponseEntity.status(OK).body(expenseService.findExpense(userDetails, page,
                ascend, minAmount, maxAmount, startDate, endDate, category));
    }
}
