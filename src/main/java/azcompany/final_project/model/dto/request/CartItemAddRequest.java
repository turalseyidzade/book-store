package azcompany.final_project.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemAddRequest {

    @NotNull(message = "Book ID must not be null")
    private Long bookId;

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be a positive")
    private Integer quantity;
}
