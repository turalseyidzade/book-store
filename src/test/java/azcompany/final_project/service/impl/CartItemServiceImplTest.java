package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.AlreadyExistsException;
import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.exception.custom.QuantityException;
import azcompany.final_project.mapper.CartItemMapper;
import azcompany.final_project.model.dto.request.CartItemAddRequest;
import azcompany.final_project.model.dto.request.CartItemUpdateRequest;
import azcompany.final_project.model.dto.response.CartItemResponse;
import azcompany.final_project.model.entity.BookEntity;
import azcompany.final_project.model.entity.CartEntity;
import azcompany.final_project.model.entity.CartItemEntity;
import azcompany.final_project.model.entity.UserEntity;
import azcompany.final_project.repository.BookRepository;
import azcompany.final_project.repository.CartItemRepository;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.util.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceImplTest {

    @Mock private BookRepository bookRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private CartItemMapper cartItemMapper;
    @Mock private SecurityUtil securityUtil;

    @InjectMocks
    private CartItemServiceImpl cartItemService;

    private UserEntity user;
    private CartEntity cart;
    private BookEntity book;
    private CartItemEntity cartItem;
    private CartItemResponse cartItemResponse;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder().build();
        user.setId(1L);

        cart = new CartEntity();
        cart.setId(10L);
        cart.setUser(user);
        cart.setItems(new ArrayList<>());

        book = BookEntity.builder()
                .title("Effective Java")
                .stockCount(10)
                .build();
        book.setId(100L);

        cartItem = CartItemEntity.builder()
                .cart(cart)
                .book(book)
                .quantity(2)
                .build();
        cartItem.setId(500L);

        cartItemResponse = new CartItemResponse();
    }

    @Test
    void addItem_Success() {
        // Arrange
        CartItemAddRequest request = new CartItemAddRequest(100L, 3);
        when(securityUtil.getUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));
        when(cartItemRepository.save(any(CartItemEntity.class))).thenReturn(cartItem);
        when(cartItemMapper.toResponse(any())).thenReturn(cartItemResponse);

        CartItemResponse result = cartItemService.addItem(request);

        assertNotNull(result);
        verify(cartItemRepository).save(any());
        assertTrue(cart.getItems().size() > 0);
    }

    @Test
    void addItem_AlreadyExists_ShouldThrowException() {
        cart.getItems().add(cartItem);
        CartItemAddRequest request = new CartItemAddRequest(100L, 1);

        when(securityUtil.getUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));

        assertThrows(AlreadyExistsException.class, () -> cartItemService.addItem(request));
    }

    @Test
    void addItem_QuantityExceedsStock_ShouldThrowException() {
        CartItemAddRequest request = new CartItemAddRequest(100L, 15);
        when(securityUtil.getUser()).thenReturn(user);
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(100L)).thenReturn(Optional.of(book));

        assertThrows(QuantityException.class, () -> cartItemService.addItem(request));
    }

    @Test
    void getItemById_Success_ShouldValidateAccess() {
        when(cartItemRepository.findById(500L)).thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toResponse(cartItem)).thenReturn(cartItemResponse);

        cartItemService.getItemById(500L);

        verify(securityUtil).validateAccess(user);
        verify(cartItemMapper).toResponse(cartItem);
    }

    @Test
    void updateItemById_Success() {
        CartItemUpdateRequest request = new CartItemUpdateRequest(5);
        when(cartItemRepository.findById(500L)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(any())).thenReturn(cartItem);
        when(cartItemMapper.toResponse(any())).thenReturn(cartItemResponse);

        cartItemService.updateItemById(500L, request);

        assertEquals(5, cartItem.getQuantity());
        verify(securityUtil).validateAccess(user);
    }

    @Test
    void deleteItemById_Success() {
        when(cartItemRepository.findById(500L)).thenReturn(Optional.of(cartItem));

        cartItemService.deleteItemById(500L);

        verify(securityUtil).validateAccess(user);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    void getItemById_NotFound_ShouldThrowException() {
        when(cartItemRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cartItemService.getItemById(999L));
    }
}