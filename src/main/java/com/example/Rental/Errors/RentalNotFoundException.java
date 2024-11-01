package com.example.Rental.Errors;


public class RentalNotFoundException extends RuntimeException {
    public RentalNotFoundException(Long rentalId) {
        super("Rental with ID " + rentalId + " not found.");
    }
}