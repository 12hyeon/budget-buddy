package hyeon.buddy.controller;

import hyeon.buddy.dto.CategoryRequestDTO;
import hyeon.buddy.dto.CategoryResponseDTO;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Tag(name = "User API", description = "유저와 관련된 API")
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "저장된 카테고리를 조회합니다.")
    public ResponseEntity<CategoryResponseDTO> findCategories() {
        return ResponseEntity.status(OK).body(categoryService.findCategories());
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "원하는 새 카테고리를 추가합니다.") // 최대 10개
    public ResponseEntity<ExceptionResponse> saveCategory(@RequestBody @Valid CategoryRequestDTO dto) {
        return ResponseEntity.status(OK).body(categoryService.saveCategory(dto));
    }

}