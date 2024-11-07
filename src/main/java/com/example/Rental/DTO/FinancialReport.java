package com.example.Rental.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class FinancialReport {
    private BigDecimal totalPayments;
    private BigDecimal totalRentals;
    private BigDecimal totalRevenue;
}
