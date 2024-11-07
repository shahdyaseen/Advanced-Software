package com.example.Rental.Controller;

import com.example.Rental.Services.RecommendationService;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.Services.ItemService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final RecommendationService recommendationService;

    @Autowired
    public ItemController(ItemService itemService,RecommendationService recommendationService) {
        this.itemService = itemService;
        this.recommendationService = recommendationService;

    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }


    @GetMapping("/search")
    public ResponseEntity<List<Item>> searchItems(@RequestParam String title, @RequestHeader("userId") Long userId) {
        List<Item> items = itemService.searchItemsByTitle(title);
        recommendationService.logUserInteraction(userId, items);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createItem(@RequestBody @NotNull Item item) {
        // Simulate image upload
        String imageUrl = "https://s3.amazonaws.com/your-bucket/" + item.getTitle() + ".jpg";
        item.setImageUrl(imageUrl);
        try {
            Item savedItem = itemService.saveItem(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error saving item: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item) {
        try {
            Item updatedItem = itemService.updateItem(id, item);
            return ResponseEntity.ok(updatedItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Item>> getItemsByCategory(@PathVariable Long categoryId) {
        List<Item> items = itemService.getItemsByCategory(categoryId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/category/{categoryId}/search")
    public ResponseEntity<List<Item>> searchItemsByCategoryAndTitle(
            @PathVariable Long categoryId,
            @RequestParam String title) {
        List<Item> items = itemService.searchItemsByCategoryAndTitle(categoryId, title);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/title-price")
    public ResponseEntity<List<Item>> searchItemsByTitleAndPriceRange(
            @RequestParam String title,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Item> items = itemService.searchItemsByTitleAndPriceRange(title, minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/price-range")
    public ResponseEntity<List<Item>> searchItemsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<Item> items = itemService.searchItemsByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/search/rating")
    public ResponseEntity<List<Item>> searchItemsByRating(
            @RequestParam double minRating) {
        List<Item> items = itemService.searchItemsByRating(minRating);
        return ResponseEntity.ok(items);
    }




}
