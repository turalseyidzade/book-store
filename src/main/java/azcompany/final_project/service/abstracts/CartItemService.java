package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.request.CartItemAddRequest;
import azcompany.final_project.model.dto.request.CartItemUpdateRequest;
import azcompany.final_project.model.dto.response.CartItemResponse;

public interface CartItemService {
    CartItemResponse addItem(CartItemAddRequest cartItemAddRequest);

    CartItemResponse getItemById(Long id);

    CartItemResponse updateItemById(Long id, CartItemUpdateRequest request);

    void deleteItemById(Long id);
}
