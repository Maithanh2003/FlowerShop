package com.mai.flower_shop.controller;

import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Category;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.category.CategoryService;
import com.mai.flower_shop.service.category.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final ICategoryService categoryService;
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();

            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("Found!")
                    .data(categories).build());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message("error!")
                    .data(INTERNAL_SERVER_ERROR)
                    .build());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category){
        try {
            Category newCategory = categoryService.addCategory(category);

            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("add success")
                    .data(newCategory).build());
        } catch (Exception e) {
            return ResponseEntity.status(CONFLICT).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null).build());
        }
    }

    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse>  getCategoryByName (@PathVariable String name){
        try {
            Category category = categoryService.getCategoryByName(name);

            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("Found")
                    .data(category).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message("Not Found")
                    .data(NOT_FOUND).build());
        }
    }

    @DeleteMapping("/category/{id}/delete")
    public ResponseEntity<ApiResponse>  deleteCategory (@PathVariable Long id){
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("Found")
                    .data(null).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message("Not Found")
                    .data(NOT_FOUND).build());
        }
    }

    @PutMapping("/category/{id}/update")
    public ResponseEntity<ApiResponse> updateCategory (@RequestBody Category category, @PathVariable Long id){
        try {
            Category categoryUpdate = categoryService.updateCategory(category,id);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("update success")
                    .data(categoryUpdate).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message("update fail")
                    .data(null).build());
        }
    }
}
