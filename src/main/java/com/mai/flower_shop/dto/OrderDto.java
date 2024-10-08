package com.mai.flower_shop.dto;

import com.mai.flower_shop.enums.OrderStatus;
import com.mai.flower_shop.model.OrderItem;
import com.mai.flower_shop.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private String status;
    private Set<OrderItemDto> orderItems;
    private Long userId;
}
