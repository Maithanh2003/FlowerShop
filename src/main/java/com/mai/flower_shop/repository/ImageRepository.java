package com.mai.flower_shop.repository;

import com.mai.flower_shop.model.Category;
import com.mai.flower_shop.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
