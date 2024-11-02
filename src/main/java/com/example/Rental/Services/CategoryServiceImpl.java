package com.example.Rental.Services;

import com.example.Rental.models.Entity.Category;
import com.example.Rental.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Transactional
    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public boolean categoryExists(String categoryName) {
        return categoryRepository.findByName(categoryName).isPresent();
    }

    @Override public List<Category> searchCategoriesByName(String name)
    { return categoryRepository.findByNameContainingIgnoreCase(name); }
}
