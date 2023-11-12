package hyeon.buddy.service;

import hyeon.buddy.domain.Category;
import hyeon.buddy.dto.CategoryRequestDTO;
import hyeon.buddy.exception.CustomException;
import hyeon.buddy.exception.ExceptionCode;
import hyeon.buddy.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("중복된 이름 - 카테코리 저장")
    public void testSaveCategory_WhenDuplicatedTitle_ThenThrowException() {
        // Given
        CategoryRequestDTO dto = new CategoryRequestDTO("Duplicated Title");
        Category existingCategory = Category.from(dto);

        // When
        when(categoryRepository.findByTitleContaining(dto.getTitle())).thenReturn(Optional.of(existingCategory));

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.saveCategory(dto));

        // Then
        assertThat(exception.getExceptionCode())
                .isEqualTo(ExceptionCode.CATEGORY_DUPLICATED_TITLE);
    }

    @Test
    @DisplayName("10개 초과 - 카테코리 저장")
    public void testSaveCategory_WhenExceededCount_ThenThrowException() {
        // Given
        CategoryRequestDTO dto = new CategoryRequestDTO("Duplicated Title");
        Category category = Category.from(dto);

        // When
        when(categoryRepository.findByTitleContaining(dto.getTitle())).thenReturn(Optional.empty());
        when(categoryRepository.count()).thenReturn(10L);

        CustomException exception = assertThrows(CustomException.class, () -> categoryService.saveCategory(dto));

        // Then
        assertThat(exception).isInstanceOf(CustomException.class);
        assertThat(exception.getExceptionCode()).isEqualTo(ExceptionCode.CATEGORY_EXCEEDED_COUNT);
    }

}
