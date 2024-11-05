package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<Item> getAllItems();
    Optional<Item> getItemById(Long id);
    Item saveItem(Item item);
    void deleteItem(Long id);
    Item updateItem(Long id, Item item);

    List<Item> searchItemsByTitle(String title);
    boolean hasItemsForCategory(Long categoryId);
    void deleteItemsByCategory(Long categoryId);

    List<Item> getItemsByCategory(Long categoryId);

    List<Item> searchItemsByCategoryAndTitle(Long categoryId, String title);

    List<Item> searchItemsByTitleAndPriceRange(String title, BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> searchItemsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    List<Item> searchItemsByRating(double rating);
}
