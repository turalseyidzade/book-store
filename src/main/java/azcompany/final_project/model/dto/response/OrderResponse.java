package azcompany.final_project.model.dto.response;

import azcompany.final_project.model.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private List<OrderItemResponse> orderItems;
    private BigDecimal totalPrice;
    private OrderStatus status;
}
