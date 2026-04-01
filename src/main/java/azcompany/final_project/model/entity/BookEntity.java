package azcompany.final_project.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class BookEntity extends BaseEntity {
    private String title;
    private String author;

    @Column(unique = true)
    private UUID isbn;
    private String description;
    private BigDecimal price;
    private Integer pageCount;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity category;

    @ManyToOne(fetch = FetchType.LAZY)
    private BaseFileEntity image;
}
