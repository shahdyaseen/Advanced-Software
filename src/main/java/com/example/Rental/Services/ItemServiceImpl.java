package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.RecommendationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final RecommendationRepository recommendationsRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, RecommendationRepository recommendationsRepository) {
        this.itemRepository = itemRepository;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public Item saveItem(Item item) {
        if (item.getCategory() == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        return itemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        // Delete related recommendations first
        recommendationsRepository.deleteByItemId(id);
        itemRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Item updateItem(Long id, Item item) {
        return itemRepository.findById(id).map(existingItem -> {
            if (item.getTitle() != null) {
                existingItem.setTitle(item.getTitle());
            }
            if (item.getQuantity() != null) {
                existingItem.setQuantity(item.getQuantity());
            }
            if (item.getDescription() != null) {
                existingItem.setDescription(item.getDescription());
            }
            if (item.getPricePerDay() != null) {
                existingItem.setPricePerDay(item.getPricePerDay());
            }
            if (item.getAvailabilityStatus() != null) {
                existingItem.setAvailabilityStatus(item.getAvailabilityStatus());
            }
            if (item.getCategory() != null) {
                existingItem.setCategory(item.getCategory());
            }
            return itemRepository.save(existingItem);
        }).orElseThrow(() -> new IllegalArgumentException("Item not found with id: " + id));
    }

    public List<Item> searchItemsByTitle(String title) {
        List<Item> results = new ArrayList<>();
        String[] keywords = title.split(" "); // Split the input by space

        for (String keyword : keywords) {
            results.addAll(itemRepository.findByTitleContainingIgnoreCase(keyword));
        }

        return results.stream().distinct().collect(Collectors.toList()); // To avoid duplicates
    }

    @Override
    public boolean hasItemsForCategory(Long categoryId) {
        return itemRepository.existsByCategoryId(categoryId);
    }

    @Override
    public void deleteItemsByCategory(Long categoryId) {
        itemRepository.deleteByCategoryId(categoryId);
    }

    @Override
    public List<Item> getItemsByCategory(Long categoryId) {
        return itemRepository.findByCategory_Id(categoryId);
    }

    @Override
    public List<Item> searchItemsByCategoryAndTitle(Long categoryId, String title) {
        // Use searchItemsByTitle to get items by title keywords and filter by category
        return searchItemsByTitle(title).stream()
                .filter(item -> item.getCategory() != null && item.getCategory().getId().equals(categoryId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsByTitleAndPriceRange(String title, BigDecimal minPrice, BigDecimal maxPrice) {
        // Use searchItemsByTitle to get items by title keywords and filter by price range
        return searchItemsByTitle(title).stream()
                .filter(item -> item.getPricePerDay().compareTo(minPrice) >= 0 && item.getPricePerDay().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        // Retrieve all items and filter by price range
        return itemRepository.findAll().stream()
                .filter(item -> item.getPricePerDay().compareTo(minPrice) >= 0 && item.getPricePerDay().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItemsByRating(double rating) {
        // Retrieve all items and filter by rating
        return itemRepository.findAll().stream()
                .filter(item -> item.getAverageRating() >= rating)
                .collect(Collectors.toList());
    }
}
