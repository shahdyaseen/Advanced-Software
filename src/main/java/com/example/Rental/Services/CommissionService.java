package com.example.Rental.Services;

import com.example.Rental.Errors.CommissionNotFoundException;
import com.example.Rental.Errors.DateRangeException;
import com.example.Rental.models.Entity.Commission;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.repositories.CommissionRepository;
import com.example.Rental.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public CommissionService(CommissionRepository commissionRepository ,RentalRepository rentalRepository) {
        this.commissionRepository = commissionRepository;
        this.rentalRepository = rentalRepository;
    }

    // Method to calculate and save a commission for a rental
    public Commission calculateAndSaveCommission(Rental rental) {
        BigDecimal commissionRate = new BigDecimal("0.10"); // 10%
        BigDecimal commissionAmount = rental.getTotalPrice().multiply(commissionRate);

        Commission commission = new Commission();
        commission.setRental(rental);
        commission.setAmount(commissionAmount);
        commission.setUpdatedAt(LocalDateTime.now());

        return commissionRepository.save(commission);
    }

    // Method to retrieve all commissions
    public List<Commission> getAllCommissions() {
        return commissionRepository.findAll();
    }

    // Method to get a commission by ID
    public Optional<Commission> getCommissionById(Long id) {
        return commissionRepository.findById(id);
    }

    // Method to update an existing commission
    public Optional<Commission> updateCommission(Long id, Commission commissionDetails) {
        return commissionRepository.findById(id).map(commission -> {
            commission.setAmount(commissionDetails.getAmount());

            // Keep the existing rental if not provided in the update request
            if (commissionDetails.getRental() != null) {
                Rental rental = rentalRepository.findById(commissionDetails.getRental().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid rental ID"));
                commission.setRental(rental);
            }

            commission.setUpdatedAt(LocalDateTime.now());
            return commissionRepository.save(commission);
        });
    }


    // Method to delete a commission
    public void deleteCommission(Long id) {
        if (!commissionRepository.existsById(id)) {
            throw new CommissionNotFoundException(id);
        }
        commissionRepository.deleteById(id);
    }

    // Method to get total commission amount
    public BigDecimal getTotalCommission() {
        List<Commission> commissions = commissionRepository.findAll();
        if (commissions.isEmpty()) {
            throw new CommissionNotFoundException(null);
        }
        return commissions.stream()
                .map(Commission::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Commission> getCommissionsByFlexibleDateRange(
            Integer year, Integer month, Integer day,
            String start, String end,
            Integer startYear, Integer endYear) {

        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        // Specific year, month, day
        if (year != null && month == null && day == null) {
            startDateTime = LocalDateTime.of(year, 1, 1, 0, 0);
            endDateTime = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        } else if (year != null && month != null && day == null) {
            startDateTime = LocalDateTime.of(year, month, 1, 0, 0);
            endDateTime = LocalDateTime.of(year, month, LocalDate.of(year, month, 1).lengthOfMonth(), 23, 59, 59);
        } else if (year != null && month != null && day != null) {
            startDateTime = LocalDateTime.of(year, month, day, 0, 0);
            endDateTime = startDateTime.withHour(23).withMinute(59).withSecond(59);
        }
        // Specific month only (ignores year)
        else if (month != null && year == null && day == null) {
            startDateTime = LocalDateTime.of(LocalDate.now().getYear(), month, 1, 0, 0);
            endDateTime = LocalDateTime.of(LocalDate.now().getYear(), month, LocalDate.of(LocalDate.now().getYear(), month, 1).lengthOfMonth(), 23, 59, 59);
        }
        // Specific day only (ignores year and month)
        else if (day != null && month == null && year == null) {
            int currentYear = LocalDate.now().getYear();
            int currentMonth = LocalDate.now().getMonthValue();
            startDateTime = LocalDateTime.of(currentYear, currentMonth, day, 0, 0);
            endDateTime = startDateTime.withHour(23).withMinute(59).withSecond(59);
        }
        // Date range with start and end
        else if (start != null && end != null) {
            try {
                startDateTime = LocalDate.parse(start).atStartOfDay();
                endDateTime = LocalDate.parse(end).atTime(23, 59, 59);
            } catch (DateTimeParseException e) {
                throw new DateRangeException();
            }
        }
        // Start and end with year range only
        else if (startYear != null && endYear != null) {
            startDateTime = LocalDateTime.of(startYear, 1, 1, 0, 0);
            endDateTime = LocalDateTime.of(endYear, 12, 31, 23, 59, 59);
        } else {
            throw new DateRangeException();
        }

        if (startDateTime.isAfter(endDateTime)) {
            throw new DateRangeException();
        }

        List<Commission> commissions = commissionRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        if (commissions.isEmpty()) {
            throw new CommissionNotFoundException(null);
        }
        return commissions;
    }
    public Commission calculateAndSaveCommission(Rental rental, BigDecimal commissionAmount) {
        Commission commission = new Commission();
        commission.setRental(rental);
        commission.setAmount(commissionAmount);

        return commissionRepository.save(commission);
    }
}
