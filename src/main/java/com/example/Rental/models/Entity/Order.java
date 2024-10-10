package com.example.Rental.models.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "`order`")  // Use backticks instead of single quotes
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private LocalDateTime orderDate;
    /*private LocalDateTime rentalStartDate;
    private LocalDateTime rentalEndDate;*/     //delete comment

    private Double totalAmount;
    private Boolean isReturned = false;


    // getters and setters
}
