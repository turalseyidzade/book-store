package azcompany.final_project.controller;

import azcompany.final_project.model.dto.request.CartItemAddRequest;
import azcompany.final_project.model.dto.request.CartItemUpdateRequest;
import azcompany.final_project.model.dto.response.CartItemResponse;
import azcompany.final_project.service.abstracts.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/cart-items")
public class CartItemController {
    private final CartItemService cartItemService;

    @PostMapping("/{cartId}")
    public ResponseEntity<CartItemResponse> addItem(
            @PathVariable Long cartId,
            @RequestBody @Valid CartItemAddRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartItemService.addItem(cartId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartItemResponse> getCartItemById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartItemService.getItemById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> updateCartItemById(@PathVariable Long id, @RequestBody @Valid CartItemUpdateRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartItemService.updateItemById(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItemById(@PathVariable Long id) {
        cartItemService.deleteItemById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
