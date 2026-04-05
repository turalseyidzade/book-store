package azcompany.final_project.repository;

import azcompany.final_project.model.entity.CartEntity;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    @NullMarked
    @EntityGraph(attributePaths = {"user", "items"})
    Optional<CartEntity> findById(Long id);
}
