package com.mai.flower_shop.service.image;

import com.mai.flower_shop.dto.ImageDto;
import com.mai.flower_shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    void updateImage(MultipartFile file, Long id);

    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);
}
