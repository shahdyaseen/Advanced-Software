package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.AvailabilityStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Item entity representing an item available for rent in the rental system.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_day", nullable = false)
    private BigDecimal pricePerDay;


    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "availability_status", nullable = false, columnDefinition = "ENUM('AVAILABLE', 'UNAVAILABLE', 'RESERVED') DEFAULT 'AVAILABLE'")
    private AvailabilityStatus availabilityStatus = AvailabilityStatus.AVAILABLE;



    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;



    public boolean isAvailable() {
        return availabilityStatus == AvailabilityStatus.AVAILABLE;
    }


    private double averageRating;
    @Column(
            name = "review_count",
            nullable = false
    )
    private int reviewCount;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "item_tags",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tags = new HashSet<>();

    public Item(Long itemId) {
        this.averageRating = 0.0;
        this.reviewCount = 0;
        this.id = itemId;
    }


}


