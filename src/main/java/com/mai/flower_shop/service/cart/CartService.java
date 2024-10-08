package com.mai.flower_shop.service.cart;

import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.repository.CartItemRepository;
import com.mai.flower_shop.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart not found")
        );
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);

    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        // vi bo nho van luu giu items
        cart.getItems().clear();
        cartRepository.deleteById(id);

    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
    @Override
    public Long initializeNewCart() {
        Cart cart = new Cart();
        Long newCartId = cartIdGenerator.incrementAndGet();
        cart.setId(newCartId);
        return cartRepository.save(cart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId (userId);
    }
}
