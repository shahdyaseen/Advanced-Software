package com.example.Rental.Services.PaymentProcessors;

import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Enumes.PaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentProcessor implements PaymentProcessor {

    @Override
    public Payment processPayment(PaymentRequest request, BigDecimal amount) {
        Payment payment = new Payment();
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setAmount(amount);
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setTransactionId("ST-" + System.currentTimeMillis());
        return payment;
    }
}
