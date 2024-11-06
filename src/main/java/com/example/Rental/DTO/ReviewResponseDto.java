package com.example.Rental.DTO;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    private Long rentalId;
    private String userName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewResponseDto(Long rentalId, String userName, int rating, String comment, LocalDateTime createdAt) {
        this.rentalId = rentalId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public Long getRentalId() {
        return this.rentalId;
    }

    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
}
}