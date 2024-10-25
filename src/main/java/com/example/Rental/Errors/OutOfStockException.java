package com.example.Rental.Errors;


public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String message) {
        super(message);
    }

}
