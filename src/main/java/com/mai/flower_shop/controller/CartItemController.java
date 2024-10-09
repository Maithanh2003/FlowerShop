package com.mai.flower_shop.controller;

import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.cart.ICartItemService;
import com.mai.flower_shop.service.cart.ICartService;
import com.mai.flower_shop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/cartItems")
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;
    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart ( @RequestParam Long productId, @RequestParam Integer quantity){
        try {
            User user = userService.getUserById(4L);
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(null)
                    .message("add item success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage()).build());
        }
    }
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFromCart(cartId, itemId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(null)
                    .message("Remove Item Success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage()).build());
        }

    }
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity (@PathVariable Long cartId, @PathVariable Long itemId ,
                                                           @RequestParam Integer quantity){
        try {
            cartItemService.updateItemQuantity(cartId,itemId, quantity);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(null)
                    .message("Update Item Success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .data(null)
                    .message(e.getMessage()).build());
        }
    }
}
