package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.CategoryMapper;
import azcompany.final_project.model.dto.request.CategoryRequest;
import azcompany.final_project.model.dto.response.CategoryResponse;
import azcompany.final_project.model.entity.CategoryEntity;
import azcompany.final_project.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryEntity categoryEntity;
    private CategoryResponse categoryResponse;
    private CategoryRequest categoryRequest;

    @BeforeEach
    void setUp() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);
        categoryEntity.setName("Bədii Ədəbiyyat");

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(1L);
        categoryResponse.setName("Bədii Ədəbiyyat");

        categoryRequest = new CategoryRequest();
        categoryRequest.setName("Bədii Ədəbiyyat");
    }

    @Test
    void save_Success() {
        when(categoryMapper.toEntity(categoryRequest)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toResponse(categoryEntity)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.save(categoryRequest);

        assertNotNull(result);
        assertEquals(categoryResponse.getName(), result.getName());
        verify(categoryRepository).save(any(CategoryEntity.class));
    }

    @Test
    void getAll_Success() {
        List<CategoryEntity> entities = List.of(categoryEntity);
        when(categoryRepository.findAll()).thenReturn(entities);
        when(categoryMapper.toResponse(any(CategoryEntity.class))).thenReturn(categoryResponse);

        List<CategoryResponse> result = categoryService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
    }

    @Test
    void getById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.toResponse(categoryEntity)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getById_NotFound_ShouldThrowNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.getById(99L));
    }

    @Test
    void updateById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(categoryMapper.updateRequestToEntity(categoryRequest, categoryEntity)).thenReturn(categoryEntity);
        when(categoryRepository.save(categoryEntity)).thenReturn(categoryEntity);
        when(categoryMapper.toResponse(categoryEntity)).thenReturn(categoryResponse);

        CategoryResponse result = categoryService.updateById(categoryRequest, 1L);

        assertNotNull(result);
        verify(categoryRepository).save(categoryEntity);
    }

    @Test
    void deleteById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));

        categoryService.deleteById(1L);

        verify(categoryRepository).delete(categoryEntity);
    }

    @Test
    void deleteById_NotFound_ShouldThrowNotFoundException() {
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteById(1L));
        verify(categoryRepository, never()).delete(any());
    }
}