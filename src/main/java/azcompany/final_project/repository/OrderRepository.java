package azcompany.final_project.repository;

import azcompany.final_project.model.entity.OrderEntity;
import azcompany.final_project.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByUser(UserEntity user);
}
