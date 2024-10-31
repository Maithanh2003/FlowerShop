package com.mai.flower_shop.controller;

import com.mai.flower_shop.dto.CartDto;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.repository.UserRepository;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.cart.CartService;
import com.mai.flower_shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {
    private final ICartService cartService;
    private final UserRepository userRepository;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(cartDto)
                    .message("success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage()).build());
        }
    }
    @GetMapping("/user/{userId}/my-cart")
    public ResponseEntity<ApiResponse> getUserCart( @PathVariable Long userId){
        try {
            Cart cart = cartService.getCartByUserId(userId);
            CartDto cartDto = cartService.convertToDto(cart);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(cartDto)
                    .message("success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage()).build());
        }
    }
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart (@PathVariable Long cartId){
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(null)
                    .message("clear success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message("total price").build());
        }
    }
    @GetMapping("/{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount (@PathVariable Long cartId){
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(totalAmount)
                    .message("total price").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message("total price").build());

        }
    }

}
