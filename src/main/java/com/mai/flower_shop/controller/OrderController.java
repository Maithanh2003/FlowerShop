package com.mai.flower_shop.controller;

import com.mai.flower_shop.dto.OrderDto;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Order;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder (@RequestParam Long userId){
        try {
            Order order = orderService.placeOrder(userId);
            OrderDto orderDto = orderService.convertToDto(order);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(orderDto)
                    .message("Order success").build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .data(e.getMessage())
                            .message("Error Occured").build());
        }
    }
    @GetMapping("{orderId}/order")
    public ResponseEntity<ApiResponse> getOrderById (@PathVariable Long orderId){
        try {
            OrderDto order =  orderService.getOrder(orderId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(order)
                    .message("get Order success").build());
            } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(ApiResponse.builder()
                            .data(e.getMessage())
                            .message("error").build());
        }
    }
    @GetMapping("{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders (@PathVariable Long userId){
        try {
            List<OrderDto> orderList =  orderService.getUserOrders(userId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(orderList)
                    .message("get Order list success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(ApiResponse.builder()
                            .data(e.getMessage())
                            .message("error").build());
        }
    }
}
