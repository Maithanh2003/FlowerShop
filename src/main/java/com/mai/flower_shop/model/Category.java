package com.mai.flower_shop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    protected String name;
    @JsonIgnore
    @OneToMany(mappedBy = "category")
    List<Product> products;
    public Category(String name) {
        this.name = name;
    }
}
