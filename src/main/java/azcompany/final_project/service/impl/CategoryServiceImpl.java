package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.CategoryMapper;
import azcompany.final_project.model.dto.request.CategoryRequest;
import azcompany.final_project.model.dto.response.CategoryResponse;
import azcompany.final_project.model.entity.CategoryEntity;
import azcompany.final_project.repository.CategoryRepository;
import azcompany.final_project.service.abstracts.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryResponse save(CategoryRequest request) {
        log.info("CategoryService.save.start: {}", request);
        CategoryEntity entity = categoryMapper.toEntity(request);
        CategoryEntity savedEntity = categoryRepository.save(entity);
        CategoryResponse response = categoryMapper.toResponse(savedEntity);
        log.info("CategoryService.save.end: {}", response);
        return response;
    }

    @Override
    public List<CategoryResponse> getAll() {
        log.info("CategoryService.getAll.start");
        List<CategoryResponse> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList();
        log.info("CategoryService.getAll.end: {}", categories);
        return categories;
    }

    @Override
    public CategoryResponse getById(Long id) {
        log.info("CategoryService.getById.start: {}", id);
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        CategoryResponse response = categoryMapper.toResponse(entity);
        log.info("CategoryService.getById.end: {}", response);
        return response;
    }

    @Override
    public CategoryResponse updateById(CategoryRequest request, Long id) {
        log.info("CategoryService.updateById.start: {}", request);
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        entity = categoryMapper.updateRequestToEntity(request, entity);
        categoryRepository.save(entity);
        CategoryResponse response = categoryMapper.toResponse(entity);
        log.info("CategoryService.updateById.end: {}", response);
        return response;
    }

    @Override
    public void deleteById(Long id) {
        log.info("CategoryService.deleteById.start: {}", id);
        CategoryEntity entity = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + id));
        categoryRepository.delete(entity);
        log.info("CategoryService.deleteById.end: {}", entity);
    }
}
