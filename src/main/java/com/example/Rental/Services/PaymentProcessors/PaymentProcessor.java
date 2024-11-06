package com.example.Rental.Services.PaymentProcessors;

import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.models.Entity.Payment;

import java.math.BigDecimal;


public interface PaymentProcessor {
    Payment processPayment(PaymentRequest request, BigDecimal amount);
}
