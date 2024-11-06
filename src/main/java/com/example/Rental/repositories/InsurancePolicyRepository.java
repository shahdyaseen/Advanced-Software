package com.example.Rental.repositories;
import com.example.Rental.models.Entity.InsurancePolicy;
import com.example.Rental.models.Enumes.InsurancePolicyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long> {


    List<InsurancePolicy> findByStatus(InsurancePolicyStatus status);


}