package com.example.Rental.Controller;

import com.example.Rental.DTO.ReviewResponseDto;
import com.example.Rental.Services.UserServices.ReviewService;
import com.example.Rental.models.Entity.DeliveryReview;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery-reviews")
public class DeliveryReviewController {
    private final ReviewService reviewService;

    public DeliveryReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDeliveryReview(@RequestBody ReviewResponseDto reviewDto) {
        try {
            DeliveryReview review = reviewService.addDeliveryReview(reviewDto.getRentalId(), reviewDto.getRating(), reviewDto.getComment());
            return new ResponseEntity<>(review, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editDeliveryReview(@PathVariable Long id, @RequestBody ReviewResponseDto reviewDto) {
        try {
            DeliveryReview updatedReview = reviewService.editDeliveryReview(id, reviewDto.getRating(), reviewDto.getComment());
            return new ResponseEntity<>(updatedReview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDeliveryReview(@PathVariable Long id) {
        try {
            reviewService.deleteDeliveryReview(id);
            return new ResponseEntity<>("Review deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/delivery/{deliveryId}")
    public ResponseEntity<?> getReviewsByDeliveryId(@PathVariable Long deliveryId) {
        List<DeliveryReview> reviews = reviewService.getReviewsByDeliveryId(deliveryId);
        return ResponseEntity.ok(reviews);
    }
}
