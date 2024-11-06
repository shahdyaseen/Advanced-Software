package com.example.Rental.repositories;

import com.example.Rental.models.Entity.InsuranceClaim;
import com.example.Rental.models.Enumes.InsuranceClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InsuranceClaimRepository extends JpaRepository<InsuranceClaim, Long> {

    @Override
    Optional<InsuranceClaim> findById(Long aLong);

    void deleteById(Long Id);
    List<InsuranceClaim> findByStatus(InsuranceClaimStatus status);


}