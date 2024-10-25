package com.example.Rental.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CartItemRequest {
    private Long userId;
    private Long itemId;
    private int quantity;
    private LocalDate startDate;
    private LocalDate endDate;
}
