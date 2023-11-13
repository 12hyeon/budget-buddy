package hyeon.buddy.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyeon.buddy.dto.BudgetSaveRequestDTO;
import hyeon.buddy.dto.BudgetUpdateRequestDTO;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Tag(name = "Budget API", description = "예산와 관련된 API")
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@RestController
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @Operation(summary = "예산 생성", description = "새로운 예산을 생성합니다.")
    public ResponseEntity<ExceptionResponse> saveBudget(@AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody @Valid BudgetSaveRequestDTO dto) {
        return ResponseEntity.status(OK).body(budgetService.saveBudget(userDetails, dto));
    }

    @PutMapping("/{bid}")
    @Operation(summary = "예산 수정", description = "기존 예산의 금액을 수정합니다. (다음 달 이후)")
    public ResponseEntity<ExceptionResponse> updateBudget(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long bid,
                                                          @RequestBody @Valid BudgetUpdateRequestDTO dto) {
        return ResponseEntity.status(OK).body(budgetService.updateBudget(userDetails, bid, dto));
    }

    @DeleteMapping("/{bid}")
    @Operation(summary = "예산 삭제", description = "기존 예산을 삭제합니다. (다음 달 이후)")
    public ResponseEntity<ExceptionResponse> deleteBudget(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable Long bid) {
        return ResponseEntity.status(OK).body(budgetService.deleteBudget(userDetails, bid));
    }

    @GetMapping
    @Operation(summary = "예산 조회", description = "날짜에 따라 생성한 예산을 조회합니다.")
    public ResponseEntity<ExceptionResponse> findBudget(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "true") Boolean ascend,
            @RequestParam(defaultValue = "0") int minAmount,
            @RequestParam(defaultValue = "2000000000") int maxAmount,
            @RequestParam(defaultValue = "2023-11")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM") String startDate,
            @RequestParam(defaultValue = "2030-12")
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM") String endDate) {
        return ResponseEntity.status(OK).body(budgetService.findBudget(userDetails,
                ascend, minAmount, maxAmount, startDate, endDate));
    }
}
