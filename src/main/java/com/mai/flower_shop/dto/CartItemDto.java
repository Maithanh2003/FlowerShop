package com.mai.flower_shop.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CartItemDto {
    private Long itemId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private ProductDto product;
}
