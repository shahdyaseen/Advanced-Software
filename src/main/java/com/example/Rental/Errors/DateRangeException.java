package com.example.Rental.Errors;

public class DateRangeException extends RuntimeException {
    public DateRangeException() {
        super("Invalid date range provided.");
    }
}
