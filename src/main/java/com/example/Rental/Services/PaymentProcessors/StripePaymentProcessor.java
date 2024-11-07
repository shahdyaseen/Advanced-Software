package com.example.Rental.Services.PaymentProcessors;

import com.example.Rental.DTO.PaymentRequest;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentProcessor {

    public StripePaymentProcessor() {
        Stripe.apiKey = "sk_test_51QHXq0E6bYKa0K42YrlfLWGL2OQBNpp875TeTzgfPqMMGSljXtAdlUQUnXv0r9ZLpUwIbiWnoEcPF4HNIivQcmVu00cb4NO281";
    }

    public void processPayment(PaymentRequest request, BigDecimal amount) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", amount.multiply(new BigDecimal("100")).intValue());
            params.put("currency", "usd");
            params.put("payment_method", request.getStripeToken());
            params.put("confirmation_method", "manual");
            params.put("confirm", true);

            PaymentIntent intent = PaymentIntent.create(params);

            if (intent.getStatus().equals("succeeded")) {
                System.out.println("Payment successful with ID: " + intent.getId());
            } else {
                throw new RuntimeException("Payment failed: " + intent.getStatus());
            }

        } catch (Exception e) {
            throw new RuntimeException("Stripe payment failed: " + e.getMessage(), e);
        }
    }
}
