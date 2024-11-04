package com.example.Rental.Errors;

public class CommissionNotFoundException extends RuntimeException {
    public CommissionNotFoundException(Long id) {
        super("Commission not found with ID: " + id);
    }
}
