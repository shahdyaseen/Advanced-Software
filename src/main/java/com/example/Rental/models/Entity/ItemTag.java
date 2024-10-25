package com.example.Rental.models.Entity;

import com.example.Rental.models.Entity.Item;
import jakarta.persistence.*;

@Entity
@Table(name = "item_tags")
public class ItemTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, length = 50)
    private String tag;

}
