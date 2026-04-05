package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.response.OrderResponse;
import azcompany.final_project.model.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final UserMapper userMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderResponse toResponse(OrderEntity order) {
        return OrderResponse.builder()
                .id(order.getId())
                .user(userMapper.toResponse(order.getUser()))
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemMapper::toResponse)
                        .toList())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .build();
    }
}
