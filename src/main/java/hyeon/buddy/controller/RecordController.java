package hyeon.buddy.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import hyeon.buddy.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Statistics API", description = "통계와 관련된 API")
@RequestMapping("/api/v1/statistics")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final RecordService recordService;

    @GetMapping
    @Operation(summary = "통계 기록 조회", description = "과거 지출 통계 정보를 확인합니다.")
    public ResponseEntity<Object> findStatistics(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam(defaultValue =  "0") int page,
                                                 @RequestParam(defaultValue = "true") Boolean ascend,
                                                 @RequestParam(defaultValue = "0") int minAmount,
                                                 @RequestParam(defaultValue = "2000000000") int maxAmount,
                                                 @RequestParam(defaultValue = "2023-11-01", value = "startDate (2023-11-01)")
                                                     @JsonFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                 @RequestParam(defaultValue = "2030-12-31", value = "startDate (2030-12-31)")
                                                     @JsonFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                 @RequestParam(required = false) Long category) {

        return ResponseEntity.status(OK).body(recordService.findRecords(userDetails, page,
                ascend, minAmount, maxAmount, startDate, endDate, category));
    }
}