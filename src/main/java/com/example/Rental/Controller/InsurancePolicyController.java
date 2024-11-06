package com.example.Rental.Controller;

import com.example.Rental.models.Entity.InsurancePolicy;
import com.example.Rental.models.Enumes.InsurancePolicyStatus;
import com.example.Rental.Services.UserServices.InsurancePolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/policies")
public class InsurancePolicyController {

    @Autowired
    private InsurancePolicyService policyService;

    @PostMapping("/USER/createPolicy")
    public ResponseEntity<InsurancePolicy> createPolicy(@RequestBody InsurancePolicy policy) {
        return ResponseEntity.ok(policyService.createPolicy(policy));
    }

    @GetMapping("/ADMIN/getAllPolicies")
    public ResponseEntity<List<InsurancePolicy>> getAllPolicies() {
        return ResponseEntity.ok(policyService.getAllPolicies());
    }

    @GetMapping("/ADMIN/getPolicyById/{id}")
    public ResponseEntity<InsurancePolicy> getPolicyById(@PathVariable Long id) {
        return policyService.getPolicyById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/COMPANY/{id}/status")
    public ResponseEntity<InsurancePolicy> updatePolicyStatus(@PathVariable Long id, @RequestParam InsurancePolicyStatus status) {
        return ResponseEntity.ok(policyService.updatePolicyStatus(id, status));
    }

   // @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @DeleteMapping("/COMPANY/deletePolicy/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();
    }

   // @PreAuthorize("hasRole('USER') or hasRole('COMPANY')")
    @PutMapping("/USER/{id}/updateDetails")
    public ResponseEntity<InsurancePolicy> updatePolicyDetails(@PathVariable Long id, @RequestBody InsurancePolicy policyDetails) {
        return policyService.updatePolicyDetails(id, policyDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


   // @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY')")
    @GetMapping("/ADMIN/findByStatus")
    public ResponseEntity<List<InsurancePolicy>> findPoliciesByStatusForADMIN(@RequestParam InsurancePolicyStatus status) {
        return ResponseEntity.ok(policyService.findPoliciesByStatus(status));
    }
    @GetMapping("/COMPANY/findByStatus")
    public ResponseEntity<List<InsurancePolicy>> findPoliciesByStatusForCOMPANY(@RequestParam InsurancePolicyStatus status) {
        return ResponseEntity.ok(policyService.findPoliciesByStatus(status));
    }

   // @PreAuthorize("hasRole('USER')")
//    @GetMapping("/user/{userId}/policies")
//    public ResponseEntity<List<InsurancePolicy>> getUserPolicies(@PathVariable Long userId) {
//        return ResponseEntity.ok(policyService.getUserPolicies(userId));
//    }


    //@PreAuthorize("hasRole('COMPANY')")
    @PutMapping("/COMPANY/{id}/updateCoverageAmount")
    public ResponseEntity<InsurancePolicy> updateCoverageAmount(@PathVariable Long id, @RequestParam double coverageAmount) {
        return policyService.updateCoverageAmount(id, coverageAmount)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}

