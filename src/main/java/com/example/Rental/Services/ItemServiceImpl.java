package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
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
    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

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






    @Override
    public List<Item> searchItemsByTitle(String title) {
        return itemRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public boolean hasItemsForCategory(Long categoryId) {
        return itemRepository.existsByCategoryId(categoryId);
    }

    @Override
    public void deleteItemsByCategory(Long categoryId) {
        itemRepository.deleteByCategoryId(categoryId);
    }
}
