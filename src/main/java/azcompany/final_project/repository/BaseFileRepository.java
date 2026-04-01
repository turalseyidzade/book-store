package azcompany.final_project.repository;

import azcompany.final_project.model.entity.BaseFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseFileRepository extends JpaRepository<BaseFileEntity, Long> {
}
