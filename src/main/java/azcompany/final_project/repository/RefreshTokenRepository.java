package azcompany.final_project.repository;

import azcompany.final_project.model.entity.RefreshTokenEntity;
import azcompany.final_project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByUser(UserEntity user);

    @EntityGraph(attributePaths = {"user"})
    Optional<RefreshTokenEntity> findByRefreshToken(UUID refreshToken);
}
