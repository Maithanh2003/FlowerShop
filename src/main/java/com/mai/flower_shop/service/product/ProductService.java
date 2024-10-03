package com.mai.flower_shop.service.product;

import com.mai.flower_shop.exception.ProductNotFoundException;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Category;
import com.mai.flower_shop.model.Product;
import com.mai.flower_shop.repository.CategoryRepository;
import com.mai.flower_shop.repository.ProductRepository;
import com.mai.flower_shop.request.AddProductRequest;
import com.mai.flower_shop.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet( () -> {
                    Category category1 = new Category(request.getCategory().getName());
                    return categoryRepository.save(category1);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }
    private Product createProduct(AddProductRequest request, Category category){
         Product product = Product.builder().name(request.getName())
                .price(request.getPrice())
                .brand(request.getBrand())
                .description(request.getDescription())
                .inventory(request.getInventory())
                .category(category)
                .build();
         return product;
    }
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                () -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    updateExistingProduct(product, request);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }
    private Product updateExistingProduct (Product existingProduct, UpdateProductRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand( category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }
}
