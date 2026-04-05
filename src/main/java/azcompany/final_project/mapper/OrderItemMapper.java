package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.response.OrderItemResponse;
import azcompany.final_project.model.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
    private final BookMapper bookMapper;

    public OrderItemResponse toResponse(OrderItemEntity orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .book(bookMapper.toResponse(orderItem.getBook()))
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }
}
