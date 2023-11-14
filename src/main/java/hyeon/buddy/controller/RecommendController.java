package hyeon.buddy.controller;

import hyeon.buddy.dto.RecommendMonthResponseDTO;
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
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Record API", description = "지출 합산과 관련된 API")
@RequestMapping("/api/v1/recommends")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RecommendController {

    private final RecordService recordService;

    // 자동으로 12시에 당일 소비한 내용 기반한 피드백 전달
    @GetMapping("/day")
    @Operation(summary = "당일 예산 추천", description = "카테고리별 당일 예산을 추천합니다.")
    public ResponseEntity<Object> recommendDay(@AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(OK).body(recordService.recommendToday(userDetails));
    }

    // 한달 평균 예산 추천
    @GetMapping("/month")
    @Operation(summary = "당월 예산 추천", description = "카테고리별 당월 예산을 추천합니다.")
    public ResponseEntity<RecommendMonthResponseDTO> recommendMonth(
            @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.status(OK).body(recordService.recommendMonth(userDetails));
    }
}