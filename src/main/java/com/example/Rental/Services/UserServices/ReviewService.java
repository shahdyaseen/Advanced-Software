package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.Delivery;
import com.example.Rental.models.Entity.DeliveryReview;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.Review;
import com.example.Rental.repositories.DeliveryRepository;
import com.example.Rental.repositories.DeliveryReviewRepository;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.RentalRepository;
import com.example.Rental.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RentalRepository rentalRepository;
    private final ItemRepository itemRepository;
    private final DeliveryReviewRepository deliveryReviewRepository;
    private final DeliveryRepository deliveryRepository;

    public ReviewService(ReviewRepository reviewRepository, RentalRepository rentalRepository, ItemRepository itemRepository,
                         DeliveryReviewRepository deliveryReviewRepository, DeliveryRepository deliveryRepository) {
        this.reviewRepository = reviewRepository;
        this.rentalRepository = rentalRepository;
        this.itemRepository = itemRepository;
        this.deliveryReviewRepository = deliveryReviewRepository;
        this.deliveryRepository = deliveryRepository;
    }



    public Review addReview(Long rentalId, int rating, String comment) throws Exception {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new Exception("Rental not found"));
        Review review = new Review();
        review.setRental(rental);
        review.setRating(rating);
        review.setComment(comment);
        Review savedReview = reviewRepository.save(review);
        updateItemRatingSummary(rental.getItem());
        return savedReview;
    }

    public Review editReview(Long reviewId, int rating, String comment) throws Exception {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
        review.setRating(rating);
        review.setComment(comment);
        Review updatedReview = reviewRepository.save(review);
        updateItemRatingSummary(review.getRental().getItem());
        return updatedReview;
    }

    public void deleteReview(Long reviewId) throws Exception {
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
        Item item = review.getRental().getItem();
        reviewRepository.deleteById(reviewId);
        updateItemRatingSummary(item);
    }

    public List<Review> getReviewsByItemId(Long itemId) {
        return reviewRepository.findByRentalItemId(itemId);
    }

    private void updateItemRatingSummary(Item item) {
        int reviewCount = reviewRepository.countByRentalItemId(item.getId());
        double averageRating = reviewCount > 0 ? reviewRepository.calculateAverageRating(item.getId()) : 0.0;
        item.setReviewCount(reviewCount);
        item.setAverageRating(averageRating);
        itemRepository.save(item);
    }



    public DeliveryReview addDeliveryReview(Long deliveryId, int rating, String comment) throws Exception {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> new Exception("Delivery not found"));
        DeliveryReview deliveryReview = new DeliveryReview();
        deliveryReview.setDelivery(delivery);
        deliveryReview.setRating(rating);
        deliveryReview.setComment(comment);
        return deliveryReviewRepository.save(deliveryReview);
    }

    public DeliveryReview editDeliveryReview(Long reviewId, int rating, String comment) throws Exception {
        DeliveryReview deliveryReview = deliveryReviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
        deliveryReview.setRating(rating);
        deliveryReview.setComment(comment);
        return deliveryReviewRepository.save(deliveryReview);
    }

    public void deleteDeliveryReview(Long reviewId) throws Exception {
        DeliveryReview review = deliveryReviewRepository.findById(reviewId).orElseThrow(() -> new Exception("Review not found"));
        deliveryReviewRepository.delete(review);
    }

    public List<DeliveryReview> getReviewsByDeliveryId(Long deliveryId) {
        return deliveryReviewRepository.findByDeliveryDeliveryId(deliveryId);
    }
}
