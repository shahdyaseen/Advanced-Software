package com.example.Rental.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private String title;
    private String category;
    private BigDecimal pricePerDay;
    private boolean availability;
    private String imageUrl;
    private int quantity;
    private int quantityInStock;
    private BigDecimal priceAtAddition;



}