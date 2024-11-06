package com.example.Rental.models.Entity;

import com.example.Rental.models.Enumes.InsuranceClaimStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "insurance_claims")
public class InsuranceClaim {
    @Getter

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @ManyToOne
    @JoinColumn(name = "insurance_policy_id", nullable = false)
    private InsurancePolicy insurancePolicy;

    @Getter
    @Column(nullable = false)
    private Double amountClaimed;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsuranceClaimStatus status = InsuranceClaimStatus.SUBMITTED;

    @Getter
    @Column
    private String description;

    @Getter
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Getter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();



    public void setStatus(InsuranceClaimStatus status) {
        this.status=status;
    }

    public void setDescription(String description) {
        this.description=description;
    }
    public void setUpdatedAt(){
        this.updatedAt=LocalDateTime.now();
    }

    public double getAmountClaimed() {
        return  amountClaimed;
    }

    public void setCreatedAt() {
        this.createdAt=LocalDateTime.now();
    }

}
