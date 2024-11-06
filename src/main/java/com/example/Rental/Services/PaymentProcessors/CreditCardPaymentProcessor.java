package com.example.Rental.Services.PaymentProcessors;

import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Enumes.PaymentStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreditCardPaymentProcessor implements PaymentProcessor {

    @Override
    public Payment processPayment(PaymentRequest request, BigDecimal amount) {
        if (isValidCreditCard(request)) {
            Payment payment = new Payment();
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setAmount(amount);
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId("CC-" + System.currentTimeMillis());
            return payment;
        } else {
            throw new RuntimeException("Invalid Credit Card Details.");
        }
    }

    private boolean isValidCreditCard(PaymentRequest request) {
        return request.getCardNumber() != null && request.getCardNumber().length() == 16
                && request.getExpiryDate() != null && request.getExpiryDate().matches("(0[1-9]|1[0-2])/\\d{2}")
                && request.getSecurityCode() != null && request.getSecurityCode().length() == 3;
    }
}
