package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.request.BookAddRequest;
import azcompany.final_project.model.dto.request.BookUpdateRequest;
import azcompany.final_project.model.dto.response.BookResponse;
import azcompany.final_project.model.entity.BookEntity;
import azcompany.final_project.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final CategoryMapper categoryMapper;
    private final FileUtil fileUtil;

    public BookEntity toEntity(BookAddRequest request) {
        return BookEntity.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .description(request.getDescription())
                .isbn(request.getIsbn())
                .price(request.getPrice())
                .pageCount(request.getPageCount())
                .stockCount(request.getStockCount())
                .build();
    }

    public BookResponse toResponse(BookEntity entity) {
        return BookResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .pageCount(entity.getPageCount())
                .stockCount(entity.getStockCount())
                .category(categoryMapper.toResponse(entity.getCategory()))
                .imageUrl(fileUtil.getFileUrl(entity.getImage()))
                .build();
    }

    public BookEntity updateRequestToEntity(BookUpdateRequest request, BookEntity entity) {
        if (request.getTitle() != null) {
            entity.setTitle(request.getTitle());
        }
        if (request.getAuthor() != null) {
            entity.setAuthor(request.getAuthor());
        }
        if (request.getDescription() != null) {
            entity.setDescription(request.getDescription());
        }
        if (request.getIsbn() != null) {
            entity.setIsbn(request.getIsbn());
        }
        if (request.getPrice() != null) {
            entity.setPrice(request.getPrice());
        }
        if (request.getPageCount() != null) {
            entity.setPageCount(request.getPageCount());
        }
        if (request.getStockCount() != null) {
            entity.setStockCount(request.getStockCount());
        }
        return entity;
    }
}
