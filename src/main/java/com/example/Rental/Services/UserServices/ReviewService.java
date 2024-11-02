package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.Review;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.RentalRepository;
import com.example.Rental.repositories.ReviewRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RentalRepository rentalRepository;
    private final ItemRepository itemRepository;

    public ReviewService(ReviewRepository reviewRepository, RentalRepository rentalRepository, ItemRepository itemRepository) {
        this.reviewRepository = reviewRepository;
        this.rentalRepository = rentalRepository;
        this.itemRepository = itemRepository;
    }

    public Review addReview(Long rentalId, int rating, String comment) throws Exception {
        Rental rental = (Rental)this.rentalRepository.findById(rentalId).orElseThrow(() -> {
            return new Exception("Rental not found");
        });
        Review review = new Review();
        review.setRental(rental);
        review.setRating(rating);
        review.setComment(comment);
        Review savedReview = (Review)this.reviewRepository.save(review);
        this.updateItemRatingSummary(rental.getItem());
        return savedReview;
    }

    public Review editReview(Long reviewId, int rating, String comment) throws Exception {
        Review review = (Review)this.reviewRepository.findById(reviewId).orElseThrow(() -> {
            return new Exception("Review not found");
        });
        review.setRating(rating);
        review.setComment(comment);
        Review updatedReview = (Review)this.reviewRepository.save(review);
        this.updateItemRatingSummary(review.getRental().getItem());
        return updatedReview;
    }

    public void deleteReview(Long reviewId) throws Exception {
        Review review = (Review)this.reviewRepository.findById(reviewId).orElseThrow(() -> {
            return new Exception("Review not found");
        });
        Item item = review.getRental().getItem();
        this.reviewRepository.deleteById(reviewId);
        this.updateItemRatingSummary(item);
    }

    public List<Review> getReviewsByItemId(Long itemId) {
        return this.reviewRepository.findByRentalItemId(itemId);
    }

    private void updateItemRatingSummary(Item item) {
        int reviewCount = this.reviewRepository.countByRentalItemId(item.getId());
        double averageRating = reviewCount > 0 ? this.reviewRepository.calculateAverageRating(item.getId()) : 0.0;
        item.setReviewCount(reviewCount);
        item.setAverageRating(averageRating);
        this.itemRepository.save(item);
    }
}