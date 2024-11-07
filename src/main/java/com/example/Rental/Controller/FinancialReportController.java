package com.example.Rental.Controller;

import com.example.Rental.DTO.FinancialReport;
import com.example.Rental.Services.FinancialReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/reports")
public class FinancialReportController {

    @Autowired
    private FinancialReportService financialReportService;

    @GetMapping("/financial")
    public ResponseEntity<FinancialReport> getFinancialReport(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        FinancialReport report = financialReportService.generateFullReport(startDate, endDate);
        return ResponseEntity.ok(report);
    }
}
