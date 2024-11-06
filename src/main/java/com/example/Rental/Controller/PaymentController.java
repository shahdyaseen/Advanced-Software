package com.example.Rental.Controller;


import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.Services.UserServices.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest paymentRequest) {
        try {
            paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok("Payment processed successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing payment: " + e.getMessage());
        }
    }


}

