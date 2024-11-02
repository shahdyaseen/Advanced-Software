package com.example.Rental.Errors;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(Long userId, Long cartItemId) {
        super("Unauthorized access by user " + userId + " to cart item " + cartItemId);
    }

}
