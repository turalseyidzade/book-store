package azcompany.final_project.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class BookAddRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @NotBlank(message = "Author must not be blank")
    private String author;

    @NotNull(message = "ISBN must not be null")
    private UUID isbn;

    @NotBlank(message = "Description must not be blank")
    private String description;

    @NotNull(message = "Price must not be null")
    private BigDecimal price;

    @NotNull(message = "Page count must not be null")
    @Positive(message = "Page count must be a positive")
    private Integer pageCount;

    @NotNull(message = "Stock count must not be null")
    @Positive(message = "Stock count must be a positive")
    private Integer stockCount;

    @NotNull(message = "Category ID must not be null")
    private Long categoryId;

    @NotNull(message = "Image ID must not be null")
    private Long imageId;
}
