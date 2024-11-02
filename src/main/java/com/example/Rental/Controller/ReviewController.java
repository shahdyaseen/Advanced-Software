package com.example.Rental.Controller;


import com.example.Rental.DTO.ItemReviewsResponseDto;
import com.example.Rental.DTO.ReviewResponseDto;
import com.example.Rental.Services.UserServices.ReviewService;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Review;
import com.example.Rental.repositories.ItemRepository;
import com.example.Rental.repositories.ReviewRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/reviews"})
public class ReviewController {
    private final ReviewService reviewService;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewService reviewService, ItemRepository itemRepository, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.itemRepository = itemRepository;
        this.reviewRepository = reviewRepository;
    }

    @PutMapping({"/add"})
    public ResponseEntity<?> addReview(@RequestBody ReviewResponseDto reviewDto) {
        try {
            Review review = this.reviewService.addReview(reviewDto.getRentalId(), reviewDto.getRating(), reviewDto.getComment());
            return new ResponseEntity(review, HttpStatus.CREATED);
        } catch (Exception var3) {
            return new ResponseEntity(var3.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping({"/edit/{id}"})
    public ResponseEntity<?> editReview(@PathVariable Long id, @RequestBody ReviewResponseDto reviewDto) {
        try {
            Review updatedReview = this.reviewService.editReview(id, reviewDto.getRating(), reviewDto.getComment());
            return new ResponseEntity(updatedReview, HttpStatus.OK);
        } catch (Exception var4) {
            return new ResponseEntity(var4.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping({"/delete/{id}"})
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            this.reviewService.deleteReview(id);
            return new ResponseEntity("Review deleted successfully", HttpStatus.OK);
        } catch (Exception var3) {
            return new ResponseEntity(var3.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping({"/item/{itemId}"})
    public ResponseEntity<?> getReviewsByItemId(@PathVariable Long itemId) {
        Item item = (Item)this.itemRepository.findById(itemId).orElse((Item) null);
        if (item == null) {
            return new ResponseEntity("Item not found", HttpStatus.NOT_FOUND);
        } else {
            List<Review> reviews = this.reviewRepository.findByRentalItemId(itemId);
            List<ReviewResponseDto> reviewResponseDtos = (List) reviews.stream().map((review) -> {
                return new ReviewResponseDto(review.getRental().getId(), review.getRental().getRenter().getUsername(), review.getRating(), review.getComment(), review.getCreatedAt());
            }).collect(Collectors.toList());
            return ResponseEntity.ok(new ItemReviewsResponseDto(item.getTitle(), reviewResponseDtos));
        }
    }
}