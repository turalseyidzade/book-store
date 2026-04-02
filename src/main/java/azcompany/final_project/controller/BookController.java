package azcompany.final_project.controller;

import azcompany.final_project.model.dto.request.BookAddRequest;
import azcompany.final_project.model.dto.request.BookUpdateRequest;
import azcompany.final_project.model.dto.response.BookResponse;
import azcompany.final_project.service.abstracts.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/books")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody @Valid BookAddRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.save(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(@RequestParam(required = false) Long categoryId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.getAll(categoryId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBookById(
            @PathVariable Long id,
            @RequestBody @Valid BookUpdateRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(bookService.updateById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        bookService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
