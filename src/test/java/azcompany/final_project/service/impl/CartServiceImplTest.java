package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.CartMapper;
import azcompany.final_project.model.dto.response.CartResponse;
import azcompany.final_project.model.entity.CartEntity;
import azcompany.final_project.model.entity.CartItemEntity;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock private CartMapper cartMapper;
    @Mock private SecurityUtil securityUtil;

    @InjectMocks
    private CartServiceImpl cartService;

    private UserEntity user;
    private CartEntity cart;
    private CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        user = new UserEntity();
        user.setId(1L);
        user.setEmail("test@gmail.com");

        cart = new CartEntity();
        cart.setId(10L);
        cart.setUser(user);
        List<CartItemEntity> items = new ArrayList<>();
        items.add(new CartItemEntity());
        cart.setItems(items);

        cartResponse = new CartResponse();
    }

    @Test
    void getCartById_Success() {
        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));
        when(cartMapper.toResponse(cart)).thenReturn(cartResponse);

        CartResponse result = cartService.getCartById(10L);

        assertNotNull(result);
        verify(securityUtil).validateAccess(user);
        verify(cartRepository).findById(10L);
    }

    @Test
    void getCartById_NotFound_ShouldThrowNotFoundException() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.getCartById(99L));
        verify(securityUtil, never()).validateAccess(any());
    }

    @Test
    void clearCartById_Success_ShouldEmptyItems() {
        assertEquals(1, cart.getItems().size());

        when(cartRepository.findById(10L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);
        when(cartMapper.toResponse(cart)).thenReturn(cartResponse);

        CartResponse result = cartService.clearCartById(10L);

        assertTrue(cart.getItems().isEmpty());
        verify(securityUtil).validateAccess(user);
        verify(cartRepository).save(cart);
        assertNotNull(result);
    }

    @Test
    void clearCartById_NotFound_ShouldThrowException() {
        when(cartRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartService.clearCartById(99L));
        verify(cartRepository, never()).save(any());
    }
}