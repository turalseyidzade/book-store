package azcompany.final_project.repository;

import azcompany.final_project.model.entity.BookEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    @NullMarked
    @EntityGraph(attributePaths = {"category", "image"})
    Optional<BookEntity> findById(Long id);

    @EntityGraph(attributePaths = {"image"})
    List<BookEntity> findAllByCategoryId(Long categoryId);

    @NullMarked
    @EntityGraph(attributePaths = {"category", "image"})
    List<BookEntity> findAll();
}
