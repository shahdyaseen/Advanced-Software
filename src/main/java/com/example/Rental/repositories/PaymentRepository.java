package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

