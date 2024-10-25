package com.example.Rental.models.Entity;

import com.example.Rental.models.Entity.Category;
import jakarta.persistence.*;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Table(name = "products")
public class Product {
// this class should remove, use item class instead of . if you need

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ProductId")
    private Long productId;
    @Column(name="Name")
    private String name;
    @Column(name="Description")
    private String description;
    @Column(name="Price")
    private Double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private Boolean isAvailable = true;


}

