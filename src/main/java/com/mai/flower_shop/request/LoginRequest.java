package com.mai.flower_shop.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LoginRequest {
    @NotBlank
    @NotBlank(message = "Invalid credentials")
    private String email;
    @NotBlank
    @NotBlank(message = "Invalid credentials")
    private String password;
}