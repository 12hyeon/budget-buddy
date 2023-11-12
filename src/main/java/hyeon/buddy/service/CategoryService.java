package hyeon.buddy.service;

import hyeon.buddy.dto.CategoryRequestDTO;
import hyeon.buddy.dto.CategoryResponseDTO;
import hyeon.buddy.exception.ExceptionResponse;

public interface CategoryService {

    ExceptionResponse saveCategory(CategoryRequestDTO dto);
    CategoryResponseDTO findCategories();

}
