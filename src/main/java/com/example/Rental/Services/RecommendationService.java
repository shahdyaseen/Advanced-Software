package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Recommendations;
import com.example.Rental.models.Entity.User;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.RecommendationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository, ItemRepository itemRepository) {
        this.recommendationRepository = recommendationRepository;
        this.itemRepository = itemRepository;
    }

    public void logUserInteraction(Long userId, List<Item> items) {
        for (Item item : items) {
            saveRecommendation(userId, item);
        }
    }

    public void logUserRentalInteraction(Long userId, Long itemId) {
        itemRepository.findById(itemId).ifPresent(item -> saveRecommendation(userId, item));
    }

    private void saveRecommendation(Long userId, Item item) {
        if (!recommendationRepository.existsByUser_UserIdAndItem_Id(userId, item.getId())) {
            User user = new User(userId); // Create a User object with the given userId
            Recommendations recommendation = new Recommendations(user, item);
            recommendationRepository.save(recommendation);
        }
    }

    public List<Item> getRecommendationsForUser(Long userId) {
        List<Recommendations> recommendations = recommendationRepository.findByUser_UserId(userId);
        return recommendations.stream()
                .map(Recommendations::getItem)
                .distinct()
                .toList();
    }

    // get items based on similar names
    public List<Item> getRecommendationsByName(Long userId) {
        List<Item> userItems = getUserItems(userId);
        Set<Item> recommendedItems = new HashSet<>();

        for (Item item : userItems) {
            List<Item> similarItems = itemRepository.findByTitleContainingIgnoreCase(item.getTitle());
            recommendedItems.addAll(similarItems);
        }
        return List.copyOf(recommendedItems);
    }

    //  get items based on the same category
    public List<Item> getRecommendationsByCategory(Long userId) {
        List<Item> userItems = getUserItems(userId);
        Set<Item> recommendedItems = new HashSet<>();

        for (Item item : userItems) {
            List<Item> categoryItems = itemRepository.findByCategory(item.getCategory());
            recommendedItems.addAll(categoryItems);
        }
        return List.copyOf(recommendedItems);
    }



    //  get a combined list of recommended items based on all criteria
    public List<Item> getCombinedRecommendations(Long userId) {
        Set<Item> combinedRecommendations = new HashSet<>();

        combinedRecommendations.addAll(getRecommendationsByName(userId));
        combinedRecommendations.addAll(getRecommendationsByCategory(userId));

        return List.copyOf(combinedRecommendations);
    }
    private List<Item> getUserItems(Long userId) {
        // Fetch items that the user has rented or searched in the recommendation table
        List<Recommendations> userRecommendations = recommendationRepository.findByUser_UserId(userId);
        return userRecommendations.stream()
                .map(Recommendations::getItem)
                .toList();
    }

    public List<Recommendations> getAllRecommendations() {
        return recommendationRepository.findAll();
    }

    // Delete recommendations for a specific user
    @Transactional
    public void deleteRecommendationsForUser(Long userId) {
        recommendationRepository.deleteByUser_UserId(userId);
    }

    // Delete all recommendations
    public void deleteAllRecommendations() {
        recommendationRepository.deleteAll();
    }
}
