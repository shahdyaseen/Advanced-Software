package com.example.Rental.repositories;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import java.math.BigDecimal;
import java.util.List;

import java.math.BigDecimal;
import java.util.List;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByRentalId(Long rentalId);




}

