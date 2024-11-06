package com.example.Rental.Errors;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(Long userId) {
        super("No items to confirm in the cart for user " + userId);
    }
}