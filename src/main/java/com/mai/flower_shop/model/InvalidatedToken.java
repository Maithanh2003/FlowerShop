package com.mai.flower_shop.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class InvalidatedToken {
    @Id
    private String id;

    public InvalidatedToken(String id) {
        this.id = id;
    }
}