package com.example.Rental.DTO;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DeliveryRequest {
    private String address;
    private LocalDate deliveryDate;
    private BigDecimal deliveryFee;
}
