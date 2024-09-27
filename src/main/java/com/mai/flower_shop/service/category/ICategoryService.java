package com.mai.flower_shop.service.category;

import com.mai.flower_shop.model.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category updateCategory(Category category, Long id);
    Category addCategory(Category category);
    void deleteCategoryById(Long id);

}
