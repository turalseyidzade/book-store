package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.exception.custom.QuantityException;
import azcompany.final_project.mapper.OrderMapper;
import azcompany.final_project.model.dto.response.OrderResponse;
import azcompany.final_project.model.entity.*;
import azcompany.final_project.model.enums.OrderStatus;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.repository.OrderRepository;
import azcompany.final_project.repository.UserRepository;
import azcompany.final_project.service.abstracts.CartService;
import azcompany.final_project.service.abstracts.OrderService;
import azcompany.final_project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final SecurityUtil securityUtil;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderResponse placeOrder() {
        log.info("OrderService.placeOrder.start");
        UserEntity user = securityUtil.getUser();
        OrderEntity order = new OrderEntity();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        CartEntity cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        for (CartItemEntity cartItem : cart.getItems()) {
            if (cartItem.getQuantity() > cartItem.getBook().getStockCount()) {
                throw new QuantityException("Quantity exceeded");
            }
            OrderItemEntity orderItem = createOrderItem(cartItem, order);
            order.getOrderItems().add(orderItem);
            BookEntity book = cartItem.getBook();
            book.setStockCount(book.getStockCount() - cartItem.getQuantity());
        }
        cartService.clearCartById(cart.getId());
        order.setStatus(OrderStatus.COMPLETED);
        order.setTotalPrice(order.getOrderItems().stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        OrderEntity savedEntity = orderRepository.save(order);
        OrderResponse orderResponse = orderMapper.toResponse(savedEntity);
        log.info("OrderService.placeOrder.end");
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        log.info("OrderService.getAllOrders.start");
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        securityUtil.validateAccess(user);
        List<OrderEntity> orders = orderRepository.findAllByUser(user);
        List<OrderResponse> response = orders.stream()
                .map(orderMapper::toResponse)
                .toList();
        log.info("OrderService.getAllOrders.end");
        return response;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        log.info("OrderService.getOrderById.start");
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        securityUtil.validateAccess(order.getUser());
        OrderResponse orderResponse = orderMapper.toResponse(order);
        log.info("OrderService.getOrderById.end");
        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrderById(Long id) {
        log.info("OrderService.cancelOrderById.start");
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        securityUtil.validateAccess(order.getUser());
        order.setStatus(OrderStatus.CANCELED);
        for (OrderItemEntity orderItem : order.getOrderItems()) {
            BookEntity book = orderItem.getBook();
            book.setStockCount(book.getStockCount() + orderItem.getQuantity());
        }
        OrderEntity savedEntity = orderRepository.save(order);
        OrderResponse orderResponse = orderMapper.toResponse(savedEntity);
        log.info("OrderService.cancelOrderById.end");
        return orderResponse;
    }

    private OrderItemEntity createOrderItem(CartItemEntity cartItem, OrderEntity order) {
        return OrderItemEntity.builder()
                .order(order)
                .book(cartItem.getBook())
                .quantity(cartItem.getQuantity())
                .price(cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .build();
    }
}
