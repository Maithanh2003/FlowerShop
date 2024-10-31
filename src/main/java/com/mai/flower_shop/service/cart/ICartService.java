package com.mai.flower_shop.service.cart;

import com.mai.flower_shop.dto.CartDto;
import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);

    CartDto convertToDto(Cart cart);
}
