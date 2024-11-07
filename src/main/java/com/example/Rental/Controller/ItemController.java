package com.example.Rental.Controller;

import com.example.Rental.Services.ItemServiceImpl;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.Services.ItemService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final ItemServiceImpl itemServiceI;


    @Autowired
    public ItemController(ItemService itemService, ItemServiceImpl itemServiceI) {
        this.itemService = itemService;
        this.itemServiceI = itemServiceI;
    }

    @GetMapping
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }


    @GetMapping("/search")
    public List<Item> searchItems(@RequestParam String title) {
        return itemService.searchItemsByTitle(title);
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


    @PostMapping("/{itemId}/tags")
    public ResponseEntity<Item> addTagsToItem(@PathVariable Long itemId, @RequestBody Set<String> tagNames) {
        Item updatedItem = itemServiceI.addTagsToItem(itemId, tagNames);
        return ResponseEntity.ok(updatedItem);
    }
    @GetMapping("/tags/{tagName}")
    public ResponseEntity<List<Item>> getItemsByTag(@PathVariable String tagName) {
        List<Item> items = itemServiceI.getItemsByTag(tagName);
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity.ok(items);
    }
}
