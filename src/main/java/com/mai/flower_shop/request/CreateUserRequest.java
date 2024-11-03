package com.mai.flower_shop.request;

import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.Order;
import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotEmpty(message = "firstName không được bỏ trống")
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

