package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.response.CartItemResponse;
import azcompany.final_project.model.entity.CartItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CartItemMapper {
    private final BookMapper bookMapper;

    public CartItemResponse toResponse(CartItemEntity entity) {
        return CartItemResponse.builder()
                .id(entity.getId())
                .book(bookMapper.toResponse(entity.getBook()))
                .quantity(entity.getQuantity())
                .price(entity.getBook().getPrice().multiply(BigDecimal.valueOf(entity.getQuantity())))
                .build();
    }
}
