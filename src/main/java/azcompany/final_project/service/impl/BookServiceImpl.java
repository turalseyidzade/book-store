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
import azcompany.final_project.service.abstracts.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final CategoryRepository categoryRepository;
    private final BaseFileRepository baseFileRepository;
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookResponse save(BookAddRequest request) {
        log.info("BookService.save.start: {}", request);
        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategoryId()));
        BaseFileEntity baseFile = baseFileRepository.findById(request.getImageId())
                .orElseThrow(() -> new NotFoundException("Image not found with id: " + request.getImageId()));

        BookEntity book = bookMapper.toEntity(request);
        book.setCategory(category);
        book.setImage(baseFile);
        BookEntity savedEntity = bookRepository.save(book);
        BookResponse bookResponse = bookMapper.toResponse(savedEntity);
        log.info("BookService.save.end: {}", bookResponse);
        return bookResponse;
    }

    @Override
    public BookResponse getById(Long id) {
        log.info("BookService.getById.start: {}", id);
        BookEntity book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        BookResponse bookResponse = bookMapper.toResponse(book);
        log.info("BookService.getById.end: {}", bookResponse);
        return bookResponse;
    }

    @Override
    public List<BookResponse> getAll(Long categoryId) {
        log.info("BookService.getAll.start: {}", categoryId);
        List<BookEntity> books;
        if (categoryId == null) {
            books = bookRepository.findAll();
        } else {
            CategoryEntity category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
            books = bookRepository.findAllByCategoryId(category.getId());
        }
        List<BookResponse> responses = books.stream()
                .map(bookMapper::toResponse)
                .toList();
        log.info("BookService.getAll.end: {}", responses);
        return responses;
    }

    @Override
    public BookResponse updateById(Long id, BookUpdateRequest request) {
        log.info("BookService.updateById.start: {}", id);
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        entity = bookMapper.updateRequestToEntity(request, entity);
        if (request.getCategoryId() != null) {
            CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category not found with id: " + request.getCategoryId()));
            entity.setCategory(category);
        }
        if (request.getImageId() != null) {
            BaseFileEntity baseFile = baseFileRepository.findById(request.getImageId())
                    .orElseThrow(() -> new NotFoundException("Image not found with id: " + request.getImageId()));
            entity.setImage(baseFile);
        }
        BookEntity savedEntity = bookRepository.save(entity);
        BookResponse bookResponse = bookMapper.toResponse(savedEntity);
        log.info("BookService.updateById.end: {}", bookResponse);
        return bookResponse;
    }

    @Override
    public void deleteById(Long id) {
        log.info("BookService.deleteById.start: {}", id);
        BookEntity entity = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        bookRepository.delete(entity);
        log.info("BookService.deleteById.end: {}", id);
    }
}
