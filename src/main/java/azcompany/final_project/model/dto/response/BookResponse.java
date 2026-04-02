package azcompany.final_project.model.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
    private Long id;
    private String title;
    private String author;
    private UUID isbn;
    private String description;
    private BigDecimal price;
    private Integer pageCount;
    private Integer stockCount;
    private CategoryResponse category;
    private String imageUrl;
}
