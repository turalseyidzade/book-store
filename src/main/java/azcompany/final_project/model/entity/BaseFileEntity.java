package azcompany.final_project.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "base_files")
public class BaseFileEntity extends BaseEntity {
    private String originalName;
    private String changedFileName;
}
