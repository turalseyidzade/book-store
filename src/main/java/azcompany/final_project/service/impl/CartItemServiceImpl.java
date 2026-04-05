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
import azcompany.final_project.service.abstracts.CartItemService;
import azcompany.final_project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final BookRepository bookRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final SecurityUtil securityUtil;

    @Override
    public CartItemResponse addItem(CartItemAddRequest cartItemAddRequest) {
        log.info("CartItemService.addItem.start: {}", cartItemAddRequest);
        UserEntity user = securityUtil.getUser();
        CartEntity cartEntity = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart not found"));

        BookEntity book = bookRepository.findById(cartItemAddRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + cartItemAddRequest.getBookId()));

        for (CartItemEntity cartItem : cartEntity.getItems()) {
            if (cartItem.getBook().getId().equals(book.getId())) {
                throw new AlreadyExistsException("Book already exists in your cart");
            }
        }

        Integer quantity = cartItemAddRequest.getQuantity();
        if (quantity > book.getStockCount()) {
            throw new QuantityException("Quantity is greater than stock count");
        }
        CartItemEntity cartItemEntity = CartItemEntity.builder()
                .cart(cartEntity)
                .book(book)
                .quantity(quantity)
                .build();
        cartEntity.getItems().add(cartItemEntity);
        CartItemEntity savedEntity = cartItemRepository.save(cartItemEntity);
        CartItemResponse cartItemResponse = cartItemMapper.toResponse(savedEntity);
        log.info("CartItemService.addItem.end: {}", cartItemResponse);
        return cartItemResponse;
    }

    @Override
    public CartItemResponse getItemById(Long id) {
        log.info("CartItemService.getItemById.start: {}", id);
        CartItemEntity entity = cartItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found with id: " + id));
        securityUtil.validateAccess(entity.getCart().getUser());
        CartItemResponse cartItemResponse = cartItemMapper.toResponse(entity);
        log.info("CartItemService.getItemById.end: {}", cartItemResponse);
        return cartItemResponse;
    }

    @Override
    public CartItemResponse updateItemById(Long id, CartItemUpdateRequest request) {
        log.info("CartItemService.updateItemById.start: {}", id);
        CartItemEntity entity = cartItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found with id: " + id));
        securityUtil.validateAccess(entity.getCart().getUser());
        if (request.getQuantity() >= entity.getBook().getStockCount()) {
            throw new QuantityException("Quantity is greater than stock count");
        }
        entity.setQuantity(request.getQuantity());
        CartItemEntity savedEntity = cartItemRepository.save(entity);
        CartItemResponse cartItemResponse = cartItemMapper.toResponse(savedEntity);
        log.info("CartItemService.updateItemById.end: {}", cartItemResponse);
        return cartItemResponse;
    }

    @Override
    public void deleteItemById(Long id) {
        log.info("CartItemService.deleteItemById.start: {}", id);
        CartItemEntity entity = cartItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found with id: " + id));
        securityUtil.validateAccess(entity.getCart().getUser());
        cartItemRepository.delete(entity);
        log.info("CartItemService.deleteItemById.end: {}", entity);
    }
}
