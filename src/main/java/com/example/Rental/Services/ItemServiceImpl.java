package com.example.Rental.Services;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Tag;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, TagRepository tagRepository) {

        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
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


    @Transactional
    public Item addTagsToItem(Long itemId, Set<String> tagNames) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));

        Set<Tag> tags = item.getTags();

        // Add tags
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            tags.add(tag);
        }

        item.setTags(tags);
        return itemRepository.save(item);
    }

    public List<Item> getItemsByTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseThrow(() -> new IllegalArgumentException("Tag not found"));

        return itemRepository.findByTags(tag);
    }
}
