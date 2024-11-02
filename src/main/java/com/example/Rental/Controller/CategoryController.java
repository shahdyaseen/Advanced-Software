package com.example.Rental.Controller;

import com.example.Rental.Services.ItemService;
import com.example.Rental.models.Entity.Category;
import com.example.Rental.Services.CategoryService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ItemService itemService;

    @Autowired
    public CategoryController(CategoryService categoryService, ItemService itemService) {
        this.categoryService = categoryService;
        this.itemService = itemService;
    }
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search") public ResponseEntity<List<Category>> searchCategories(@RequestParam String name)
    { List<Category> categories = categoryService.searchCategoriesByName(name); return ResponseEntity.ok(categories); }
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @NotNull Category category) {
    // Check if the category name already exists
        if (categoryService.categoryExists(category.getName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Category with this name already exists.");
        }

        // Save the new category
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @RequestBody @NotNull Category category) {
        return categoryService.getCategoryById(id).map(existingCategory -> {
            if (category.getName() != null) {
                existingCategory.setName(category.getName());
            }
            if (category.getDescription() != null) {
                existingCategory.setDescription(category.getDescription());
            }
            existingCategory.setUpdatedAt(LocalDateTime.now());
            Category updatedCategory = categoryService.saveCategory(existingCategory);
            return ResponseEntity.ok("Category updated successfully.");
        }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category with ID " + id + " not found."));
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        if (itemService.hasItemsForCategory(id)) {
            // If there are associated items, ask for confirmation before deleting
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Category has associated items. Confirm deletion with /deleteWithItems endpoint.");
        } else {
            // No associated items, proceed to delete category
            categoryService.deleteCategory(id);
            return ResponseEntity.noContent().build();
        }
    }
    @Transactional
    @DeleteMapping("/{id}/deleteWithItems")
    public ResponseEntity<?> deleteCategoryWithItems(@PathVariable Long id) {
        if (itemService.hasItemsForCategory(id)) {
            // Delete associated items first, then delete the category
            itemService.deleteItemsByCategory(id);
        }
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
