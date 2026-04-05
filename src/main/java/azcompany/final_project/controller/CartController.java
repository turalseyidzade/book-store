package azcompany.final_project.controller;

import azcompany.final_project.model.dto.response.CartResponse;
import azcompany.final_project.service.abstracts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("/{id}")
    public ResponseEntity<CartResponse> getCart(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.getCartById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CartResponse> deleteCart(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.clearCartById(id));
    }
}
