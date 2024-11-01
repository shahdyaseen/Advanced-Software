package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countByRentalItemId(Long itemId);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.rental.item.id = :itemId")
    double calculateAverageRating(Long itemId);

    List<Review> findByRentalItemId(Long itemId);
}