package azcompany.final_project.repository;

import azcompany.final_project.model.entity.CartItemEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {

    @NullMarked
    @EntityGraph(attributePaths = {"book", "book.category", "book.image"})
    Optional<CartItemEntity> findById(Long id);
}
