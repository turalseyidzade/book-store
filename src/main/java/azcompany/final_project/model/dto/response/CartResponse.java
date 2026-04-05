package azcompany.final_project.model.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private UserResponse user;
    private List<CartItemResponse> items;
    private BigDecimal total;
}
