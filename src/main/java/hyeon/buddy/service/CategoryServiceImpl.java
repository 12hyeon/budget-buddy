package hyeon.buddy.service;

import hyeon.buddy.domain.Category;
import hyeon.buddy.dto.CategoryRequestDTO;
import hyeon.buddy.dto.CategoryResponseDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.exception.ExceptionResponse;
import hyeon.buddy.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public ExceptionResponse saveCategory(CategoryRequestDTO dto) {

        // 중복 체크
        categoryRepository.findByTitleContaining(dto.getTitle())
                .ifPresent(user -> {
                    throw new CustomException(ExceptionCode.CATEGORY_DUPLICATED_TITLE);
                });

        // 10개 미만인지 확인
        long count = categoryRepository.count();
        int maxCount = 10;
        if (count >= maxCount) {
            throw new CustomException(ExceptionCode.CATEGORY_EXCEEDED_COUNT);
        }

        categoryRepository.save(Category.from(dto));

        return new ExceptionResponse(ExceptionCode.CATEGORY_CREATED);
    }

    @Transactional
    @Override
    public CategoryResponseDTO findCategories() {

        List<String> category = categoryRepository.findAll().stream()
                .map(Category::getTitle).collect(Collectors.toList());

        return new CategoryResponseDTO(ExceptionCode.CATEGORY_FOUND_OK, category);

    }
}