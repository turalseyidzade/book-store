package azcompany.final_project.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private BookResponse book;
    private Integer quantity;
    private BigDecimal price;
}
