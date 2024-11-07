package com.example.Rental.Controller;
import com.example.Rental.DTO.DeliveryInfoResponse;
import com.example.Rental.models.Entity.Delivery;
import com.example.Rental.models.Enumes.DeliveryStatus;
import com.example.Rental.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("deliveries")
public class DeliveryController {

    @Autowired
    private DeliveryRepository deliveryRepository;


    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<DeliveryInfoResponse> getDeliveryByRentalId(@PathVariable Long rentalId) {
        Delivery delivery = deliveryRepository.findByRentalId(rentalId);

        if (delivery == null) {
            return ResponseEntity.notFound().build();
        }

        Duration remainingTime = delivery.getRemainingDeliveryTime();
        DeliveryInfoResponse response = new DeliveryInfoResponse(delivery, remainingTime);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/company-profits")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BigDecimal> calculateDeliveryCompanyProfits() {
        List<Delivery> allDeliveries = deliveryRepository.findAll();
        BigDecimal totalProfits = allDeliveries.stream()
                .map(Delivery::getDeliveryFee)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(totalProfits);
    }


    @PatchMapping("/{deliveryId}/status")
    public ResponseEntity<String> updateDeliveryStatus(
            @PathVariable Long deliveryId,
            @RequestParam DeliveryStatus newStatus) {


        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));


        delivery.setDeliveryStatus(newStatus);
        deliveryRepository.save(delivery);

        return ResponseEntity.ok("Delivery status updated successfully.");
    }
}




