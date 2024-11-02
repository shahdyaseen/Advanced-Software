package com.example.Rental.Controller;

import com.example.Rental.Errors.RentalNotFoundException;
import com.example.Rental.Services.UserServices.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping("/{rentalId}/confirm")
    public ResponseEntity<String> confirmRental(@PathVariable Long rentalId) {
        try {
            rentalService.confirmRental(rentalId);
            return ResponseEntity.ok("Rental request confirmed successfully.");
        } catch (RentalNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while confirming the rental request.");
        }
    }

    // Endpoint لرفض طلب الإيجار مع توضيح السبب
    @PostMapping("/{rentalId}/reject")
    public ResponseEntity<String> rejectRental(
            @PathVariable Long rentalId,
            @RequestBody Map<String, String> requestBody) {
        String notes = requestBody.get("notes");

        rentalService.rejectRental(rentalId, notes);
        return ResponseEntity.ok("Rental request rejected successfully.");
    }
}
