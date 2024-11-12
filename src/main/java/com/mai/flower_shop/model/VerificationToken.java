package com.mai.flower_shop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date expiryDate = new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000); // 24 giờ

    public VerificationToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
}