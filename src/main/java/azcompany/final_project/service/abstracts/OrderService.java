package azcompany.final_project.service.abstracts;

import azcompany.final_project.model.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse placeOrder();

    List<OrderResponse> getAllOrders(Long userId);

    OrderResponse getOrderById(Long id);

    OrderResponse cancelOrderById(Long id);
}
