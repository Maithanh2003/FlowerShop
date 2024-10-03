package com.mai.flower_shop.controller;


import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Category;
import com.mai.flower_shop.model.Product;
import com.mai.flower_shop.request.AddProductRequest;
import com.mai.flower_shop.request.UpdateProductRequest;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;
    @GetMapping("/all")

    public ResponseEntity<ApiResponse> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok().body(ApiResponse.builder()
                .message("success")
                .data(products)
                .build());
    }
    @GetMapping("product/{productId}/product")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId){
        try {
            Product product = productService.getProductById(productId);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(product)
                    .build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product){
        try {
            Product newProduct = productService.addProduct(product);
                return ResponseEntity.ok().body(ApiResponse.builder()
                        .message("Add product success!")
                        .data(newProduct)
                        .build());
        } catch (Exception e) {
            return  ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId){
        try {
            Product product = productService.updateProduct(request, productId);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("update success")
                    .data(product).build());
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @DeleteMapping("product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("Delete product success!")
                    .data(null).build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null).build());
        }
    }
    @GetMapping("/products/by/brand-and-name")
    ResponseEntity<ApiResponse> getProductByBrandAndName (@RequestParam String brand,@RequestParam String name){
        try {
            List<Product> products = productService.getProductsByBrandAndName(brand, name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                        .message("NO PRODUCT FOUND WITH NAME and brand" + name + " " + brand)
                        .data(null)
                        .build());
            }
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("/products/by/category-and-brand")
    ResponseEntity<ApiResponse> getProductByCategoryAndBrand (@RequestParam String category,@RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                        .message("NO PRODUCT FOUND " )
                        .data(null)
                        .build());
            }
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("/product/count/by-brand/and-name")
    ResponseEntity<ApiResponse> countProductsByBrandAndName (@RequestParam String brand,@RequestParam String name){
        try {
            long product = productService.countProductsByBrandAndName(brand, name);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("product count!")
                    .data(product)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("/products/{name}/products")
    ResponseEntity<ApiResponse> getProductByName (@PathVariable String name){
        try {
            List<Product> products = productService.getProductsByName(name);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                        .message("NO PRODUCT FOUND WITH NAME " + name)
                        .data(null)
                        .build());
            }
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("product/by-brand")
    ResponseEntity<ApiResponse> findProductByBrand(@RequestParam String brand){
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                        .message("NO PRODUCT FOUND WITH brand " + brand)
                        .data(null)
                        .build());
            }
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
    @GetMapping("/product/{category}/all/products")
    public ResponseEntity<ApiResponse> findProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            if (products.isEmpty()) {
                return ResponseEntity.status(NOT_FOUND).body(ApiResponse.builder()
                        .message("No products found ")
                        .data(null)
                        .build());
            }
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .message("success")
                    .data(products)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(ApiResponse.builder()
                    .message(e.getMessage())
                    .data(null)
                    .build());
        }
    }
}
