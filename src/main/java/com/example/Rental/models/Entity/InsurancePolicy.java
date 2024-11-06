package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.InsurancePolicyStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "rental_id", nullable = false)
    private Rental rental;

    @Getter
    @Column(nullable = false, unique = true)
    private String policyNumber;

    @Getter
    @Column(nullable = false)
    private Double coverageAmount;

    @Getter
    @Column(nullable = false)
    private Double premium;

    @Getter
    @Column(nullable = false)
    private String provider;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsurancePolicyStatus status = InsurancePolicyStatus.ACTIVE;

    @Getter
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    public void setStatus(InsurancePolicyStatus status) {
        this.status=status;
    }

    public void setUpdatedAt() {
        this.updatedAt=LocalDateTime.now();
    }
    public void setCreatedAt() {
        this.createdAt=LocalDateTime.now();
    }


    public void setPolicyNumber(String policyNumber) {
        this.policyNumber=policyNumber;
    }

    public void setCoverageAmount(Double coverageAmount) {
        this.coverageAmount=coverageAmount;
    }
    public void setProvider(String provider) {
        this.provider=provider;
    }
    public void setPremium(Double premium) {
        this.premium=premium;
    }

}
