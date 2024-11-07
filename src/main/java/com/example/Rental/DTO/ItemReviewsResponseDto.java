package com.example.Rental.DTO;

import java.util.List;

public class ItemReviewsResponseDto {
    private String itemTitle;
    private List<ReviewResponseDto> reviews;

    public ItemReviewsResponseDto(String itemTitle, List<ReviewResponseDto> reviews) {
        this.itemTitle = itemTitle;
        this.reviews = reviews;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public List<ReviewResponseDto> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewResponseDto> reviews) {
        this.reviews = reviews;
    }
}