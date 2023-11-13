package hyeon.buddy.controller;

import hyeon.buddy.service.ExpenseService;
import hyeon.buddy.service.RecordService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Record API", description = "지출 합산과 관련된 API")
@RequestMapping("/api/v1/records")
@RestController
@RequiredArgsConstructor
@Slf4j
public class RecordController {

    private final ExpenseService expenseService;
    private final RecordService recordService;

    // 자동으로 12시에 당일 소비한 내용에 대한 피드백 전달


}