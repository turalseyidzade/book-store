package azcompany.final_project.mapper;

import azcompany.final_project.model.dto.response.CartResponse;
import azcompany.final_project.model.entity.CartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CartMapper {
    private final UserMapper userMapper;
    private final CartItemMapper cartItemMapper;

    public CartResponse toResponse(CartEntity entity) {
        return CartResponse.builder()
                .id(entity.getId())
                .user(userMapper.toResponse(entity.getUser()))
                .items(entity.getItems().stream()
                        .map(cartItemMapper::toResponse)
                        .toList())
                .totalPrice(entity.getItems().stream()
                        .map(item -> item.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }
}
