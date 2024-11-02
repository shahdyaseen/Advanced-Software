package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;

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
}
