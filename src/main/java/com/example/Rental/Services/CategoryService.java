package com.example.Rental.Services;

import com.example.Rental.models.Entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Long id);
    Category saveCategory(Category category);
    void deleteCategory(Long id);

    boolean categoryExists(String name);

    List<Category> searchCategoriesByName(String name);
}
