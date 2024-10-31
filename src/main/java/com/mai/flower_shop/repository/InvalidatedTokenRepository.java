package com.mai.flower_shop.repository;

import com.mai.flower_shop.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken,String> {
    boolean existsById(String id);
}
