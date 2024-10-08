package com.mai.flower_shop.dto;

import com.mai.flower_shop.model.Order;
import com.mai.flower_shop.model.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderItemDto {
    private int quantity;
    private BigDecimal price;
    private Order order;
    private Long productId;
}
