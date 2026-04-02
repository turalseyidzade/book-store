package azcompany.final_project.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookUpdateRequest {
    private String title;
    private String author;
    private UUID isbn;
    private String description;
    private BigDecimal price;
    private Integer pageCount;
    private Integer stockCount;
    private Long categoryId;
    private Long imageId;
}
