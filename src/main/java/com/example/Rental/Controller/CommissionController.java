package com.example.Rental.Controller;

import com.example.Rental.Errors.CommissionNotFoundException;
import com.example.Rental.Services.CommissionService;
import com.example.Rental.models.Entity.Commission;
import com.example.Rental.models.Entity.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commissions")
public class CommissionController {

    private final CommissionService commissionService;

    @Autowired
    public CommissionController(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    // Get all commissions
    @GetMapping
    public ResponseEntity<List<Commission>> getAllCommissions() {
        List<Commission> commissions = commissionService.getAllCommissions();
        return ResponseEntity.ok(commissions);
    }

    // Get total commission amount (total revenue)
    @GetMapping("/total")
    public ResponseEntity<BigDecimal> getTotalCommission() {
        BigDecimal totalCommission = commissionService.getTotalCommission();
        return ResponseEntity.ok(totalCommission);
    }

    // Get commissions within a specific date range
    @GetMapping("/filter")
    public ResponseEntity<List<Commission>> getCommissionsByFlexibleDateRange(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "day", required = false) Integer day,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "startYear", required = false) Integer startYear,
            @RequestParam(value = "endYear", required = false) Integer endYear) {

        List<Commission> commissions = commissionService.getCommissionsByFlexibleDateRange(
                year, month, day, start, end, startYear, endYear
        );
        return ResponseEntity.ok(commissions);
    }


    // Get a commission by ID
    @GetMapping("/{id}")
    public ResponseEntity<Commission> getCommissionById(@PathVariable Long id) {
        Commission commission = commissionService.getCommissionById(id)
                .orElseThrow(() -> new CommissionNotFoundException(id));
        return ResponseEntity.ok(commission);
    }

    // Update an existing commission
    @PutMapping("/{id}")
    public ResponseEntity<Commission> updateCommission(@PathVariable Long id, @RequestBody Commission commissionDetails) {
        Commission updatedCommission = commissionService.updateCommission(id, commissionDetails)
                .orElseThrow(() -> new CommissionNotFoundException(id));
        return ResponseEntity.ok(updatedCommission);
    }

    // Delete a commission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommission(@PathVariable Long id) {
        commissionService.deleteCommission(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
