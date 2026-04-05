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
import azcompany.final_project.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private SecurityUtil securityUtil;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private CartService cartService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private UserEntity user;
    private BookEntity book;
    private CartEntity cart;
    private CartItemEntity cartItem;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);

        book = new BookEntity();
        book.setId(1L);
        book.setPrice(BigDecimal.valueOf(100));
        book.setStockCount(10);

        cartItem = new CartItemEntity();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        cart = new CartEntity();
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(List.of(cartItem));
    }

    @Test
    void placeOrder_Success() {
        when(securityUtil.getUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponse(any(OrderEntity.class))).thenReturn(new OrderResponse());

        OrderResponse response = orderService.placeOrder();

        assertNotNull(response);
        assertEquals(8, book.getStockCount());
        verify(cartService).clearCartById(cart.getId());
        verify(orderRepository).save(any(OrderEntity.class));
    }

    @Test
    void placeOrder_QuantityException() {
        cartItem.setQuantity(20);
        when(securityUtil.getUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));

        assertThrows(QuantityException.class, () -> orderService.placeOrder());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllOrders_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findAllByUser(user)).thenReturn(List.of(new OrderEntity()));
        when(orderMapper.toResponse(any())).thenReturn(new OrderResponse());

        List<OrderResponse> responses = orderService.getAllOrders(1L);

        assertFalse(responses.isEmpty());
        verify(securityUtil).validateAccess(user);
    }

    @Test
    void getOrderById_Success() {
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(new OrderResponse());

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        verify(securityUtil).validateAccess(user);
    }

    @Test
    void cancelOrderById_Success() {
        OrderEntity order = new OrderEntity();
        order.setUser(user);
        OrderItemEntity orderItem = OrderItemEntity.builder()
                .book(book)
                .quantity(2)
                .build();
        order.setOrderItems(new ArrayList<>(List.of(orderItem)));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponse(any())).thenReturn(new OrderResponse());

        int initialStock = book.getStockCount();
        orderService.cancelOrderById(1L);

        assertEquals(initialStock + 2, book.getStockCount());
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void getOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.getOrderById(1L));
    }
}