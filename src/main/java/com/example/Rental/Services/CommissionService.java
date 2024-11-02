package com.example.Rental.Services;

import com.example.Rental.models.Entity.Commission;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.repositories.CommissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CommissionService {

    private final CommissionRepository commissionRepository;

    @Autowired
    public CommissionService(CommissionRepository commissionRepository) {
        this.commissionRepository = commissionRepository;
    }

    public Commission calculateAndSaveCommission(Rental rental, BigDecimal commissionAmount) {
        Commission commission = new Commission();
        commission.setRental(rental);
        commission.setAmount(commissionAmount);

        return commissionRepository.save(commission);
    }

    public BigDecimal getTotalCommission() {
        return commissionRepository.findAll()
                .stream()
                .map(Commission::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}