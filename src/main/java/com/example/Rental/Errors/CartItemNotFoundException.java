package com.example.Rental.Errors;

public class CartItemNotFoundException extends RuntimeException {
    public CartItemNotFoundException(Long cartItemId) {
        super("Cart item not found: " + cartItemId);
}
}