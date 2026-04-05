package azcompany.final_project.controller;

import azcompany.final_project.model.dto.response.OrderResponse;
import azcompany.final_project.service.abstracts.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder() {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.placeOrder());
    }

    @GetMapping("/my-orders/{userId}")
    public ResponseEntity<List<OrderResponse>> getAllOrders(@PathVariable Long userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAllOrders(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrderById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrderResponse> cancelOrderById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.cancelOrderById(id));
    }
}
