package com.example.Rental.Controller;

import com.example.Rental.DTO.CartItemRequest;
import com.example.Rental.DTO.CartItemResponse;
import com.example.Rental.Errors.*;
import com.example.Rental.Services.UserServices.ShoppingCartService;
import com.example.Rental.models.Entity.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/ShoppingCart")
public class ShoppingCartController {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

    @Autowired
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PutMapping("/item")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemRequest request) {
        if (request.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than zero.");
        }
        try {
            ShoppingCart cartItem = shoppingCartService.addItemToCart(
                    request.getUserId(), request.getItemId(), request.getQuantity(),
                    request.getStartDate(), request.getEndDate()
            );
            logger.info("Successfully added item to cart: User ID={}, Item ID={}, Quantity={}, Start Date={}, End Date={}",
                    request.getUserId(), request.getItemId(), request.getQuantity(), request.getStartDate(), request.getEndDate());
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (ItemNotFoundException e) {
            logger.error("Failed to add item to cart: Item not found. Item ID={}", request.getItemId(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        } catch  (OutOfStockException e) {
            int availableQuantity = shoppingCartService.getAvailableQuantity(request.getItemId());
            String message = String.format("Unable to add item to cart; insufficient stock for Item ID=%d. You can add up to %d items.",
                    request.getItemId(), availableQuantity);

            logger.error("Insufficient stock for Item ID={} requested by User ID={}. Available quantity: {}.",
                    request.getItemId(), request.getUserId(), availableQuantity, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        } catch (Exception e) {
            logger.error("Failed to add item to cart: Unexpected error.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable Long userId) {
        try {
            if (!shoppingCartService.userExists(userId)) {
                logger.warn("User whit ID={} not found", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }

            List<CartItemResponse> cartItems = shoppingCartService.getCartItems(userId);
            logger.info("Retrieved cart items for User ID={}", userId);

            if (cartItems.isEmpty()) {
                logger.warn("No items found in cart for User ID={}.", userId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(List.of());
            }

            logger.info("Successfully retrieved {} item(s) from cart for User ID={}", cartItems.size(), userId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            logger.error("Failed to retrieve cart items for User ID={}, Error: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }


    @GetMapping("/{userId}/{categoryId}")
    public ResponseEntity<List<CartItemResponse>> getCartItemsByCategory(
            @PathVariable Long userId,
            @PathVariable Long categoryId) {

        try {
            if (!shoppingCartService.userExists(userId)) {
                logger.warn("User not found: User ID={}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }

            if (!shoppingCartService.categoryExists(categoryId)) {
                logger.warn("Category not found: Category ID={}", categoryId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
            }
            List<CartItemResponse> cartItems = shoppingCartService.getCartItemByCategory(userId, categoryId);
            logger.info("Retrieved cart items for User ID={} and Category ID={}", userId, categoryId);

            if (cartItems.isEmpty()) {
                logger.warn("No cart items found for User ID={} and Category ID={}", userId, categoryId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cartItems);
            }

            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            logger.error("Failed to retrieve cart items for User ID={} and Category ID={}", userId, categoryId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/{itemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long itemId, @RequestBody CartItemRequest request) {

        try {
            if (!shoppingCartService.userExists(request.getUserId())) {
                logger.warn("User not found: User ID={}", request.getUserId());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            if (request.getQuantity() <= 0) {
                logger.warn("Invalid quantity: Quantity must be greater than zero.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantity must be greater than zero.");
            }

            if (request.getStartDate() != null && request.getEndDate() != null && request.getEndDate().isBefore(request.getStartDate())) {
                logger.warn("Invalid date range: End date cannot be before start date.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date cannot be before start date.");
            }
            ShoppingCart updatedItem = shoppingCartService.updateCartItem(
                    request.getUserId(),
                    itemId,
                    request.getQuantity(),
                    request.getStartDate(),
                    request.getEndDate()
            );

            logger.info("Updated cart item successfully: User ID={}, Item ID={}, New Quantity={}, New Start Date={}, New End Date={}",
                    request.getUserId(), itemId, request.getQuantity(), request.getStartDate(), request.getEndDate());

            return ResponseEntity.ok(updatedItem);
        } catch (ItemNotFoundException e) {
            logger.error("Failed to update cart item: Item not found. Item ID={}", itemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        } catch (Exception e) {
            logger.error("Failed to update cart item: Unexpected error.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }


    @DeleteMapping("/{itemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long itemId) {
        try {
            if (!shoppingCartService.itemExists(itemId)) {
                logger.warn("Attempted to remove cart item that does not exist: Item ID={}", itemId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
            }
            shoppingCartService.removeCartItem(itemId);
            logger.info("Removed cart item successfully: Item ID={}", itemId);
            return ResponseEntity.noContent().build();
        } catch (ItemNotFoundException e) {
            logger.error("Failed to remove cart item: Item not found. Item ID={}", itemId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item not found.");
        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to remove cart item due to database constraints. Item ID={}", itemId, e);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Unable to remove item due to constraints.");
        } catch (Exception e) {
            logger.error("Failed to remove cart item: Unexpected error. Item ID={}", itemId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @GetMapping("/total/{userId}")
    public ResponseEntity<?> getTotalPrice(@PathVariable Long userId) {
        try {
            if (!shoppingCartService.userExists(userId)) {
                logger.warn("User not found: User ID={}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            BigDecimal total = shoppingCartService.calculateTotalPrice(userId);

            if (total == null || total.compareTo(BigDecimal.ZERO) <= 0) {
                logger.warn("No items found in cart for User ID={}. Total price is zero.", userId);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No items in cart.");
            }

            logger.info("Calculated total price for User ID={}: {}", userId, total);
            return ResponseEntity.ok(total);
        } catch (DataAccessException e) {
            logger.error("Database error occurred while calculating total price for User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Database error occurred.");
        } catch (Exception e) {
            logger.error("Failed to calculate total price for User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    @PutMapping("/confirmItemOrder/{itemId}/{userId}")
    public ResponseEntity<?> confirmCartItem(@PathVariable Long itemId, @PathVariable Long userId) {
        try {
            ShoppingCart confirmedItem = shoppingCartService.confirmCartItem(userId, itemId);
            logger.info("Confirmed cart item: Item ID={}, User ID={}", itemId, userId);
            return ResponseEntity.ok(confirmedItem);
        } catch (CartItemNotFoundException e) {
            logger.error("Cart item not found: Item ID={}, User ID={}", itemId, userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart item not found.");
        } catch (UserNotFoundException e) {
            logger.error("User not found: User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (UnauthorizedAccessException e) {
            logger.error("Unauthorized access: Item ID={}, User ID={}", itemId, userId, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to confirm this item.");
        } catch (Exception e) {
            logger.error("Failed to confirm cart item: Item ID={}, User ID={}", itemId, userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PutMapping("/confirmCartOrder/{userId}")
    public ResponseEntity<?> confirmAllCartItems(@PathVariable Long userId) {
        try {
            List<ShoppingCart> confirmedItems = shoppingCartService.confirmAllCartItems(userId);
            logger.info("Confirmed all cart items for User ID={}", userId);
            return ResponseEntity.ok(confirmedItems);
        } catch (UserNotFoundException e) {
            logger.error("User not found: User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        } catch (CartEmptyException e) {
            logger.warn("No items to confirm for User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No items in the cart to confirm.");
        } catch (Exception e) {
            logger.error("Failed to confirm all cart items for User ID={}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }}

}