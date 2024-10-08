package com.mai.flower_shop.request;

import com.mai.flower_shop.model.Cart;
import com.mai.flower_shop.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

