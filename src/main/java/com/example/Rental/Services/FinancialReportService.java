package com.example.Rental.Services;

import com.example.Rental.DTO.FinancialReport;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.repositories.PaymentRepository;
import com.example.Rental.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class FinancialReportService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public BigDecimal generatePaymentsReport(LocalDate startDate, LocalDate endDate) {
        List<Payment> payments = paymentRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(23, 59));
        return payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal generateRentalsReport(LocalDate startDate, LocalDate endDate) {
        List<Rental> rentals = rentalRepository.findByCreatedAtBetween(startDate.atStartOfDay(), endDate.atTime(23, 59));
        return rentals.stream()
                .map(Rental::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public FinancialReport generateFullReport(LocalDate startDate, LocalDate endDate) {
        BigDecimal totalPayments = generatePaymentsReport(startDate, endDate);
        BigDecimal totalRentals = generateRentalsReport(startDate, endDate);
        return new FinancialReport(totalPayments, totalRentals, totalPayments.add(totalRentals));
    }
}
