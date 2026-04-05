package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.response.CartResponse;

public interface CartService {
    CartResponse getCartById(Long id);

    CartResponse clearCartById(Long id);
}
