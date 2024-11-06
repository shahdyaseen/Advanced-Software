package com.example.Rental.Services.UserServices;
import com.example.Rental.models.Entity.InsurancePolicy;
import com.example.Rental.repositories.InsurancePolicyRepository;
import com.example.Rental.models.Entity.InsuranceClaim;
import com.example.Rental.models.Enumes.InsuranceClaimStatus;
import com.example.Rental.repositories.InsuranceClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsuranceClaimService {

    @Autowired
    private InsuranceClaimRepository claimRepository;

    @Autowired
    private InsurancePolicyRepository insurancePolicyRepository;

    public InsuranceClaim createClaim(InsuranceClaim claim) {
        // تحقق من وجود insurancePolicy قبل الحفظ
        if (claim.getInsurancePolicy() == null || claim.getInsurancePolicy().getId() == null) {
            throw new RuntimeException("Insurance Policy ID must not be null");
        }
        // ابحث عن InsurancePolicy في قاعدة البيانات

        Optional<InsurancePolicy> policyOptional = insurancePolicyRepository.findById(claim.getInsurancePolicy().getId());
        if (policyOptional.isEmpty()) {
            throw new RuntimeException("Insurance Policy not found");
        }

        claim.setUpdatedAt();
        claim.setCreatedAt();
        return claimRepository.save(claim);
    }

    public List<InsuranceClaim> getAllClaims() {
        return claimRepository.findAll();
    }

    public Optional<InsuranceClaim> getClaimById(Long id) {

        return claimRepository.findById(id);
    }

    public InsuranceClaim updateClaimStatus(Long id, InsuranceClaimStatus status) {
        InsuranceClaim claim = claimRepository.findById(id).orElseThrow(() -> new RuntimeException("Claim ID not found"));
        claim.setStatus(status);
        claim.setUpdatedAt();
        return claimRepository.save(claim);
    }

    public InsuranceClaim updateClaimDescription(Long id, String description) {
        InsuranceClaim claim = claimRepository.findById(id).orElseThrow(() -> new RuntimeException("Claim ID not found"));
        claim.setDescription(description);
        claim.setUpdatedAt();
        InsuranceClaim savedClaim = claimRepository.save(claim);

        return claimRepository.save(savedClaim);
    }

    public void REJECTEDClaim(Long id) {
        InsuranceClaim claim = claimRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Claim not found"));
        System.out.println("Status before update: " + claim.getStatus());
        claim.setStatus(InsuranceClaimStatus.REJECTED);
        System.out.println("Status after update: " + claim.getStatus());
        claim.setUpdatedAt();
        claimRepository.save(claim);
    }


    //need to edit
    public Optional<InsuranceClaim> getClaimsByUser(Long userId) {
        return claimRepository.findById(userId);
    }

    public List<InsuranceClaim> getClaimsByStatus(InsuranceClaimStatus status) {
        return claimRepository.findByStatus(status);
    }

    public Double getTotalClaimedAmount(InsuranceClaimStatus status) {
        if (status != null) {
            return claimRepository.findByStatus(status).stream()
                    .mapToDouble(InsuranceClaim::getAmountClaimed)
                    .sum();
        } else {
            return claimRepository.findAll().stream()
                    .mapToDouble(InsuranceClaim::getAmountClaimed)
                    .sum();
        }
    }








}
