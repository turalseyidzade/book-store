package azcompany.final_project.model.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private UserResponse user;
    private List<CartItemResponse> items;
}
