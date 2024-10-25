package com.example.Rental.Services.UserServices;

import com.example.Rental.DTO.CartItemResponse;
import com.example.Rental.Errors.CategoryNotFoundException;
import com.example.Rental.Errors.ItemNotFoundException;
import com.example.Rental.Errors.OutOfStockException;
import com.example.Rental.Errors.UserNotFoundException;
import com.example.Rental.models.Entity.*;
import com.example.Rental.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository,
                               UserRepository userRepository,
                               ItemRepository itemRepository,
                               CategoryRepository categoryRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
    }

    public ShoppingCart addItemToCart(Long userId, Long itemId, int quantity, LocalDate startDate, LocalDate endDate) {
        User user = findUserById(userId);
        Item item = findItemById(itemId);
        validateItemAvailability(item, quantity);
        return createCartItem(user, item, quantity, startDate, endDate);
    }

    public List<CartItemResponse> getCartItems(Long userId) {
        User user = findUserById(userId);
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUser(user);

        return cartItems.stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    public List<CartItemResponse> getCartItemByCategory(Long userId, Long categoryId) {
        User user = findUserById(userId);
        Category category = findCategoryById(categoryId);
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUserAndCategory(user, category);

        return cartItems.stream()
                .map(this::mapToCartItemResponse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int getAvailableQuantity(Long itemId) {
        Item item = findItemById(itemId);
        return item.getQuantity();
    }

    public ShoppingCart updateCartItem(Long userId, Long itemId, Integer quantity, LocalDate startDate, LocalDate endDate) {
        ShoppingCart cartItem = shoppingCartRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found"));

        if (quantity != null) {
            validateItemAvailability(cartItem.getItem(), quantity);
            cartItem.setQuantity(quantity);
        }

        updateCartDates(cartItem, startDate, endDate);
        return shoppingCartRepository.save(cartItem);
    }

    public void removeCartItem(Long itemId) {
        shoppingCartRepository.deleteById(itemId);
    }

    public BigDecimal calculateTotalPrice(Long userId) {
        User user = findUserById(userId);
        List<ShoppingCart> cartItems = shoppingCartRepository.findByUser(user);
        return calculateTotal(cartItems);
    }

    public boolean userExists(Long userId) {
        return userRepository.existsById(userId);
    }

    public boolean categoryExists(Long categoryId) {
        return categoryRepository.existsById(categoryId);
    }

    public boolean itemExists(Long itemId) {
        return itemRepository.existsById(itemId);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("Item not found: " + itemId));
    }

    private Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }

    private void validateItemAvailability(Item item, int quantity) {
        if (item.getQuantity() < quantity) {
            throw new OutOfStockException("Item ID " + item.getId() + " is out of stock.");
        }
    }

    private ShoppingCart createCartItem(User user, Item item, int quantity, LocalDate startDate, LocalDate endDate) {
        ShoppingCart cartItem = new ShoppingCart();
        cartItem.setUser(user);
        cartItem.setItem(item);
        cartItem.setQuantity(quantity);
        cartItem.setStartDate(startDate);
        cartItem.setEndDate(endDate);
        cartItem.setPriceAtAddition(item.getPricePerDay());
        cartItem.setCategory(item.getCategory());

        return shoppingCartRepository.save(cartItem);
    }

    private CartItemResponse mapToCartItemResponse(ShoppingCart cartItem) {
        Item item = cartItem.getItem();
        if (item != null) {
            return new CartItemResponse(
                    item.getId(),
                    item.getTitle(),
                    item.getCategory().getName(),
                    item.getPricePerDay(),
                    item.isAvailable(),
                    item.getImageUrl(),
                    cartItem.getQuantity(),
                    item.getQuantity(),
                    cartItem.getPriceAtAddition()
            );
        }
        return null;
    }

    private void updateCartDates(ShoppingCart cartItem, LocalDate startDate, LocalDate endDate) {
        if (startDate != null) {
            cartItem.setStartDate(startDate);
        }
        if (endDate != null) {
            cartItem.setEndDate(endDate);
        }

    }

    private BigDecimal calculateTotal(List<ShoppingCart> cartItems) {
        return cartItems.stream()
                .map(item -> item.getPriceAtAddition().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
