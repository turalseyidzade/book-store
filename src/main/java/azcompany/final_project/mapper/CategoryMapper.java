package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.request.CategoryRequest;
import azcompany.final_project.model.dto.response.CategoryResponse;
import azcompany.final_project.model.entity.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryEntity toEntity(CategoryRequest request) {
        return CategoryEntity.builder()
                .name(request.getName())
                .build();
    }

    public CategoryResponse toResponse(CategoryEntity entity) {
        return CategoryResponse.builder()
                .name(entity.getName())
                .build();
    }

    public CategoryEntity updateRequestToEntity(CategoryRequest request, CategoryEntity entity) {
        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        return entity;
    }
}
