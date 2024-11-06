package com.example.Rental.repositories;

import com.example.Rental.models.Entity.DeliveryReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryReviewRepository extends JpaRepository<DeliveryReview, Long> {
    List<DeliveryReview> findByDeliveryDeliveryId(Long deliveryId);

    @Query("SELECT COALESCE(AVG(dr.rating), 0) FROM DeliveryReview dr WHERE dr.delivery.deliveryId = :deliveryId")
    double calculateAverageRating(Long deliveryId);
}
