package com.example.Rental.Controller;


import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.Services.UserServices.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Transactional
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok("Payment processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
        }
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable String transactionId) {
        try {
            String status = paymentService.getPaymentStatus(transactionId);
            return ResponseEntity.ok("Payment status: " + status);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Payment not found: " + e.getMessage());
        }
    }


}

