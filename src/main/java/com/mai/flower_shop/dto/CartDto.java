package com.mai.flower_shop.dto;

import com.mai.flower_shop.model.User;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
@Data
public class CartDto {
    private Long cartId;
    private Set<CartItemDto> items;
    private BigDecimal totalAmount;
}
