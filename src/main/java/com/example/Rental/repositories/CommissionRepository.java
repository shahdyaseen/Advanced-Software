package com.example.Rental.repositories;

import com.example.Rental.models.Entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    List <Commission> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}

