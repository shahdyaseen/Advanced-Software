package com.example.Rental.repositories;
import com.example.Rental.models.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findByRentalId(Long rentalId);


}

