package com.example.Rental.Services;

import com.example.Rental.models.Entity.Delivery;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Enumes.DeliveryStatus;
import com.example.Rental.repositories.DeliveryRepository;
import com.example.Rental.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public Delivery createDelivery(Long rentalId, String address, LocalDate deliveryDate, BigDecimal deliveryFee) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        Delivery delivery = new Delivery();
        delivery.setRental(rental);
        delivery.setDeliveryAddress(address);
        delivery.setDeliveryDate(deliveryDate);
        delivery.setDeliveryFee(deliveryFee);
        delivery.setDeliveryStatus(DeliveryStatus.PENDING);

        return deliveryRepository.save(delivery);
    }

    public Delivery updateDeliveryStatus(Long deliveryId, DeliveryStatus status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));

        delivery.setDeliveryStatus(status);
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getDeliveriesByRental(Long rentalId) {

        return (List<Delivery>) deliveryRepository.findByRentalId(rentalId);
    }
}