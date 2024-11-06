package com.example.Rental.Controller;

import com.example.Rental.Services.UserServices.InsuranceClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Rental.models.Entity.InsuranceClaim;
import com.example.Rental.models.Enumes.InsuranceClaimStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/claims")
public class InsuranceClaimController {

    @Autowired
    private InsuranceClaimService claimService;

    //الشخض الذي اخذ المنتج لفترة محدودة و خربه
    @PostMapping("/USER/createClaim")
    public ResponseEntity<InsuranceClaim> createClaim(@RequestBody InsuranceClaim claim) {
        System.out.println("the clim in controller here : "+claimService.createClaim(claim));
        return ResponseEntity.ok(claimService.createClaim(claim));
    }

    @PutMapping("/USER/{id}/updateDescription")
    public ResponseEntity<InsuranceClaim> updateClaimDescription(@PathVariable Long id, @RequestParam String description) {
        return ResponseEntity.ok(claimService.updateClaimDescription(id, description));
    }

    @PutMapping("/COMPANY/{id}/REJECTED")
    public ResponseEntity<String> cancelClaim(@PathVariable Long id) {
        System.out.println("Attempting to REJECT claim with ID: " + id);
        try {
            claimService.REJECTEDClaim(id);
            System.out.println("Claim REJECTED successfully");
            return ResponseEntity.ok("Claim REJECTED successfully");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Claim not found");
        }
    }


//    @GetMapping("/USER/userClaims/{userId}")
//    public ResponseEntity<Optional<InsuranceClaim>> getClaimsByUser(@PathVariable Long userId) {
//        return ResponseEntity.ok(claimService.getClaimsByUser(userId));
//    }

    @GetMapping("/USER/{id}")
    public ResponseEntity<InsuranceClaim> getClaimById(@PathVariable Long id) {

        return claimService.getClaimById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());

    }


////////////////////


    @GetMapping("/ADMIN/status")
    public ResponseEntity<List<InsuranceClaim>> getClaimsByStatus(@RequestParam InsuranceClaimStatus status) {
        return ResponseEntity.ok(claimService.getClaimsByStatus(status));
    }

    @GetMapping("/ADMIN/getAllClaims")
    public ResponseEntity<List<InsuranceClaim>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }


    @GetMapping("/COMPANY/totalClaimedAmount")
    public ResponseEntity<Double> getTotalClaimedAmount(@RequestParam(required = false) InsuranceClaimStatus status) {
        return ResponseEntity.ok(claimService.getTotalClaimedAmount(status));
    }

    @PutMapping("/COMPANY/{id}/status")
    public ResponseEntity<InsuranceClaim> updateClaimStatus(@PathVariable Long id, @RequestParam InsuranceClaimStatus status) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, status));
    }
}
