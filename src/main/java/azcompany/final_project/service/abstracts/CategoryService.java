package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.request.CategoryRequest;
import azcompany.final_project.model.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    CategoryResponse save(CategoryRequest request);

    List<CategoryResponse> getAll();

    CategoryResponse getById(Long id);

    CategoryResponse updateById(CategoryRequest request, Long id);

    void deleteById(Long id);
}
