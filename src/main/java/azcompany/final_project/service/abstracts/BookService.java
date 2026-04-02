package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.request.BookAddRequest;
import azcompany.final_project.model.dto.request.BookUpdateRequest;
import azcompany.final_project.model.dto.response.BookResponse;

import java.util.List;

public interface BookService {
    BookResponse save(BookAddRequest request);

    BookResponse getById(Long id);

    List<BookResponse> getAll(Long categoryId);

    BookResponse updateById(Long id, BookUpdateRequest request);

    void deleteById(Long id);
}
