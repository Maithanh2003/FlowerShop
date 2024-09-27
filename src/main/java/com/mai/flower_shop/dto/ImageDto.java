package com.mai.flower_shop.dto;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}

