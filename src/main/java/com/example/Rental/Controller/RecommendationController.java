package com.example.Rental.Controller;

import com.example.Rental.Services.RecommendationService;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Recommendations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/user/{userId}")
    public List<Item> getRecommendationsForUser(@PathVariable Long userId) {
        return recommendationService.getRecommendationsForUser(userId);
    }
    @GetMapping
    public ResponseEntity<List<Recommendations>> getAllRecommendations() {
        List<Recommendations> allRecommendations = recommendationService.getAllRecommendations();
        return ResponseEntity.ok(allRecommendations);
    }

    @GetMapping("/{userId}/name")
    public ResponseEntity<List<Item>> getRecommendationsByName(@PathVariable Long userId) {
        List<Item> recommendedItems = recommendationService.getRecommendationsByName(userId);
        return ResponseEntity.ok(recommendedItems);
    }

    @GetMapping("/{userId}/category")
    public ResponseEntity<List<Item>> getRecommendationsByCategory(@PathVariable Long userId) {
        List<Item> recommendedItems = recommendationService.getRecommendationsByCategory(userId);
        return ResponseEntity.ok(recommendedItems);
    }


    @GetMapping("/{userId}/combined")
    public ResponseEntity<List<Item>> getCombinedRecommendations(@PathVariable Long userId) {
        List<Item> recommendedItems = recommendationService.getCombinedRecommendations(userId);
        return ResponseEntity.ok(recommendedItems);
    }

    @DeleteMapping("/user/{userId}")
    public void deleteRecommendationsForUser(@PathVariable Long userId) {
        recommendationService.deleteRecommendationsForUser(userId);
    }

    // Endpoint to delete all recommendations
    @DeleteMapping
    public void deleteAllRecommendations() {
        recommendationService.deleteAllRecommendations();
    }
}
