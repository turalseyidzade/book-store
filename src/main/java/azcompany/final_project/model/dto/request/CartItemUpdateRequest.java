package azcompany.final_project.model.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@NotNull
@AllArgsConstructor
public class CartItemUpdateRequest {

    @NotNull(message = "Quantity must not be null")
    @Positive(message = "Quantity must be a positive")
    private Integer quantity;
}
