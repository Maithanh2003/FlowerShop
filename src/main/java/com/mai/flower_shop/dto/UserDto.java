package com.mai.flower_shop.dto;

import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.Order;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import java.util.List;

@Data

public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private CartDto cart;
    private List<OrderDto> orders;
    private boolean enabled = false;
}
