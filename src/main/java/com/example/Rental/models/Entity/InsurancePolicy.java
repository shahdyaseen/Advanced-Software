package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.InsurancePolicyStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    @Column(nullable = false)
    private Double coverageAmount;

    @Column(nullable = false)
    private Double premium;

    @Column(nullable = false)
    private String provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsurancePolicyStatus status = InsurancePolicyStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
