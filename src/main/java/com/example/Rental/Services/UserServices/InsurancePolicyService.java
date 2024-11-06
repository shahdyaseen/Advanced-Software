package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.InsurancePolicy;
import com.example.Rental.models.Entity.Item;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import com.example.Rental.models.Enumes.InsurancePolicyStatus;
import com.example.Rental.repositories.InsurancePolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InsurancePolicyService {

    @Autowired
    private InsurancePolicyRepository policyRepository;

    public InsurancePolicy createPolicy(InsurancePolicy policy) {
        policy.setCoverageAmount(policy.getPremium()*10);
       // policy.setProvider(policy.getProvider());
        policy.setUpdatedAt();
        policy.getCreatedAt();
        return policyRepository.save(policy);
    }

    public List<InsurancePolicy> getAllPolicies() {
        return policyRepository.findAll();
    }


    public Optional<InsurancePolicy> getPolicyById(Long id) {
        return policyRepository.findById(id);
    }


    //for company
    public InsurancePolicy updatePolicyStatus(Long id, InsurancePolicyStatus status) {
        InsurancePolicy policy = policyRepository.findById(id).orElseThrow(() -> new RuntimeException("Policy not found"));
        policy.setStatus(status);
        return policyRepository.save(policy);
    }

    public void deletePolicy(Long id) {
     //   CANCELLED
        InsurancePolicy policy = policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("policy not found"));
        policy.setStatus(InsurancePolicyStatus.CANCELLED);
        policy.setUpdatedAt();
        policyRepository.save(policy);

    }

    public Optional<InsurancePolicy> updatePolicyDetails(Long id, InsurancePolicy policyDetails) {


            Optional<InsurancePolicy> existingPolicy = policyRepository.findById(id);

            if (existingPolicy.isPresent()) {
                InsurancePolicy policyToUpdate = existingPolicy.get();

                if (policyDetails.getPolicyNumber() != null) {
                    policyToUpdate.setPolicyNumber(policyDetails.getPolicyNumber());
                }

                if (policyDetails.getPremium() != null) {
                    policyToUpdate.setCoverageAmount(policyDetails.getPremium()*10);
                    policyToUpdate.setPremium(policyDetails.getPremium());

                }
                    policyToUpdate.setUpdatedAt();

                if (policyDetails.getStatus() != null) {
                    policyToUpdate.setStatus(policyDetails.getStatus());
                }

                policyRepository.save(policyToUpdate);

                return Optional.of(policyToUpdate);
            } else {
                return Optional.empty();
            }
    }

    public List<InsurancePolicy> findPoliciesByStatus(InsurancePolicyStatus status) {
        return policyRepository.findByStatus(status);

    }

    public Optional<InsurancePolicy> updateCoverageAmount(Long id, double coverageAmount) {
        Optional<InsurancePolicy> existingPolicy = policyRepository.findById(id);

        if (existingPolicy.isPresent()) {
            InsurancePolicy policyToUpdate = existingPolicy.get();

            policyToUpdate.setUpdatedAt();
            policyToUpdate.setCoverageAmount(coverageAmount);

            policyRepository.save(policyToUpdate);

            return Optional.of(policyToUpdate);
        } else {
            return Optional.empty();
        }
    }



}
