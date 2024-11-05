package com.example.Rental.DTO;

import com.example.Rental.models.Entity.Delivery;

import java.time.Duration;

public  class DeliveryInfoResponse {
    private Delivery delivery;
    private Duration remainingDeliveryTime;

    public DeliveryInfoResponse(Delivery delivery, Duration remainingDeliveryTime) {
        this.delivery = delivery;
        this.remainingDeliveryTime = remainingDeliveryTime;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public Duration getRemainingDeliveryTime() {
        return remainingDeliveryTime;
    }
}

