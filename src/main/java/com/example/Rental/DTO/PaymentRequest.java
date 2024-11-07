package com.example.Rental.DTO;

import com.example.Rental.models.Enumes.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long userId;
    private Long rentalId;

    private Long RentalId;
    private PaymentMethod paymentMethod;
    private String cardNumber;
    private String expiryDate;
    private String securityCode;
    private String stripeToken;
<<<<<<< HEAD
    private BigDecimal deliveryFee;
=======
>>>>>>> 7640219 (Implement functionality to retrieve items by tag)

}
