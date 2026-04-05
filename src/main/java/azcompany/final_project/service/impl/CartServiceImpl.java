package azcompany.final_project.service.impl;

import azcompany.final_project.exception.custom.NotFoundException;
import azcompany.final_project.mapper.CartMapper;
import azcompany.final_project.model.dto.response.CartItemResponse;
import azcompany.final_project.model.dto.response.CartResponse;
import azcompany.final_project.model.entity.CartEntity;
import azcompany.final_project.repository.CartItemRepository;
import azcompany.final_project.repository.CartRepository;
import azcompany.final_project.service.abstracts.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartResponse getCartById(Long id) {
        log.info("CartService.getCart.start: {}", id);
        CartEntity entity = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found with id " + id));
        CartResponse response = cartMapper.toResponse(entity);
        log.info("CartService.getCart.end: {}", response);
        return response;
    }

    @Override
    public CartResponse clearCartById(Long id) {
        log.info("CartService.clearCart.start: {}", id);
        CartEntity entity = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found with id " + id));
        entity.getItems().clear();
        CartEntity savedEntity = cartRepository.save(entity);
        CartResponse response = cartMapper.toResponse(savedEntity);
        log.info("CartService.clearCart.end: {}", response);
        return response;
    }
}
