package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.InsuranceClaimStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Column(nullable = false)
    private Double amountClaimed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceClaimStatus status = InsuranceClaimStatus.SUBMITTED;

    @Column
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
