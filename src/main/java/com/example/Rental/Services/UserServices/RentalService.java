package com.example.Rental.Services.UserServices;

import com.example.Rental.Errors.RentalNotFoundException;
import com.example.Rental.Services.CommissionService;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import com.example.Rental.models.Enumes.PaymentMethod;
import com.example.Rental.models.Enumes.RentalStatus;
import com.example.Rental.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final NotificationServiceImpl notificationService;

    // private PaymentService paymentService;
    private final CommissionService commissionService;

    @Autowired
    public RentalService(RentalRepository rentalRepository, NotificationServiceImpl notificationService,CommissionService commissionService/* , PaymentService paymentService*/) {
        this.rentalRepository = rentalRepository;
        this.notificationService = notificationService;
        // this.paymentService=paymentService;
        this.commissionService = commissionService;
    }

    public void confirmRental(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        rental.setStatus(RentalStatus.CONFIRMED);
        rentalRepository.save(rental);

        // Calculate and save commission upon confirmation
        commissionService.calculateAndSaveCommission(rental);

        // إرسال الإشعار إلى المستأجر
        User renter = rental.getRenter();
        String message = "Your rental request for the item " + rental.getItem().getTitle() + " has been confirmed.";
        notificationService.sendNotification(renter, "Your Rental Request is Confirmed!", message, rental, rental.getItem());
    }

    public void rejectRental(Long rentalId, String notes) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new RentalNotFoundException(rentalId));

        rental.setStatus(RentalStatus.REJECTED);
        rental.setNote(notes);
        rentalRepository.save(rental);

        User renter = rental.getRenter();
        String message = "Your rental request for " + rental.getItem().getTitle() + " has been rejected. Reason: " + notes;
        notificationService.sendNotification(renter, "Rental Request Rejected", message,rental,rental.getItem());
    }


//    public void confirmRental(Rental rental, PaymentMethod paymentMethod) {
//        rental.setStatus(RentalStatus.CONFIRMED);
//       // paymentService.processInitialPayment(rental, paymentMethod);
//        rentalRepository.save(rental);
//       // Calculate and save commission upon confirmation
//        commissionService.calculateAndSaveCommission(rental);
//    }
//
//    public void deliverRental(Rental rental, PaymentMethod paymentMethod) {
//        rental.setStatus(RentalStatus.DELIVERED);
//        paymentService.processFinalPayment(rental, paymentMethod);
//        rentalRepository.save(rental);
//    }

}

