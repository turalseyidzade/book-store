package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.BookMapper;
import azcompany.final_project.model.dto.request.BookAddRequest;
import azcompany.final_project.model.dto.request.BookUpdateRequest;
import azcompany.final_project.model.dto.response.BookResponse;
import azcompany.final_project.model.entity.BaseFileEntity;
import azcompany.final_project.model.entity.BookEntity;
import azcompany.final_project.model.entity.CategoryEntity;
import azcompany.final_project.repository.BaseFileRepository;
import azcompany.final_project.repository.BookRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock private BaseFileRepository baseFileRepository;
    @Mock private BookRepository bookRepository;
    @Mock private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private BookEntity bookEntity;
    private BookResponse bookResponse;
    private CategoryEntity categoryEntity;
    private BaseFileEntity baseFileEntity;

    @BeforeEach
    void setUp() {
        categoryEntity = new CategoryEntity();
        categoryEntity.setId(1L);

        baseFileEntity = new BaseFileEntity();
        baseFileEntity.setId(1L);

        bookEntity = BookEntity.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .category(categoryEntity)
                .image(baseFileEntity)
                .build();

        bookResponse = new BookResponse();
        bookResponse.setTitle("Clean Code");
    }

    @Test
    void save_Success() {
        BookAddRequest request = new BookAddRequest();
        request.setCategoryId(1L);
        request.setImageId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(baseFileRepository.findById(1L)).thenReturn(Optional.of(baseFileEntity));
        when(bookMapper.toEntity(request)).thenReturn(bookEntity);
        when(bookRepository.save(any(BookEntity.class))).thenReturn(bookEntity);
        when(bookMapper.toResponse(bookEntity)).thenReturn(bookResponse);

        BookResponse result = bookService.save(request);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).save(any());
    }

    @Test
    void save_CategoryNotFound_ShouldThrowNotFoundException() {
        BookAddRequest request = new BookAddRequest();
        request.setCategoryId(99L);
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.save(request));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void getById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.toResponse(bookEntity)).thenReturn(bookResponse);

        BookResponse result = bookService.getById(1L);

        assertNotNull(result);
        verify(bookRepository).findById(1L);
    }

    @Test
    void getById_NotFound_ShouldThrowNotFoundException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookService.getById(1L));
    }

    @Test
    void getAll_WhenCategoryIdIsNull_ShouldReturnAllBooks() {
        List<BookEntity> books = List.of(bookEntity);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toResponse(any())).thenReturn(bookResponse);

        List<BookResponse> result = bookService.getAll(null);

        assertEquals(1, result.size());
        verify(bookRepository).findAll();
        verify(bookRepository, never()).findAllByCategoryId(any());
    }

    @Test
    void getAll_WhenCategoryIdIsProvided_ShouldReturnFilteredBooks() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(bookRepository.findAllByCategoryId(1L)).thenReturn(List.of(bookEntity));

        bookService.getAll(1L);

        verify(bookRepository).findAllByCategoryId(1L);
    }

    @Test
    void updateById_Success() {
        BookUpdateRequest request = new BookUpdateRequest();
        request.setCategoryId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));
        when(bookMapper.updateRequestToEntity(request, bookEntity)).thenReturn(bookEntity);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(bookRepository.save(bookEntity)).thenReturn(bookEntity);
        when(bookMapper.toResponse(bookEntity)).thenReturn(bookResponse);

        BookResponse result = bookService.updateById(1L, request);

        assertNotNull(result);
        verify(bookRepository).save(any());
    }

    @Test
    void deleteById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(bookEntity));

        bookService.deleteById(1L);

        verify(bookRepository).delete(bookEntity);
    }
}