package com.example.Rental.Services.UserServices;

import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.Services.CommissionService;
import com.example.Rental.Services.PaymentProcessors.*;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import com.example.Rental.models.Enumes.PaymentStatus;
import com.example.Rental.models.Enumes.RentalStatus;
import com.example.Rental.models.Enumes.Role;
import com.example.Rental.repositories.PaymentRepository;
import com.example.Rental.repositories.RentalRepository;
import com.example.Rental.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CreditCardPaymentProcessor creditCardProcessor;
    @Autowired
    private PayPalPaymentProcessor payPalProcessor;
    @Autowired
    private StripePaymentProcessor stripeProcessor;
    @Autowired
    private BankTransferPaymentProcessor bankTransferProcessor;
    private final CommissionService commissionService;

    public PaymentService(CommissionService commissionService) {
        this.commissionService = commissionService;
    }


    public void processPayment(PaymentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Rental> confirmedRentals = rentalRepository.findByRenterAndStatus(user, RentalStatus.CONFIRMED);

        BigDecimal totalAmount = confirmedRentals.stream()
                .map(Rental::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient funds for payment.");
        }

        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);


        Payment payment;
        switch (request.getPaymentMethod()) {
            case CREDIT_CARD:
                payment = creditCardProcessor.processPayment(request, totalAmount);
                break;
            case PAYPAL:
                payment = payPalProcessor.processPayment(request, totalAmount);
                break;
            case STRIPE:
                payment = stripeProcessor.processPayment(request, totalAmount);
                break;
            case BANK_TRANSFER:
                payment = bankTransferProcessor.processPayment(request, totalAmount);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + request.getPaymentMethod());
        }

        payment.setStatus(PaymentStatus.COMPLETED);

        BigDecimal totalCommission = totalAmount.multiply(new BigDecimal("0.10"));
        BigDecimal totalOwnerAmount = BigDecimal.ZERO;
        for (Rental rental : confirmedRentals) {
            rental.setStatus(RentalStatus.DELIVERED);
            payment.setRental(rental);
            paymentRepository.save(payment);

            BigDecimal rentalAmount = rental.getTotalPrice();
            BigDecimal itemOwnerCommission = rentalAmount.multiply(new BigDecimal("0.90"));
            totalOwnerAmount = totalOwnerAmount.add(itemOwnerCommission);

            User itemOwner = rental.getItem().getUser();
            itemOwner.setBalance(itemOwner.getBalance().add(itemOwnerCommission));
            userRepository.save(itemOwner);
        }

        List<User> adminUsers = userRepository.findByRole(Role.ADMIN);
        if (!adminUsers.isEmpty()) {
            User adminUser = adminUsers.get(0);
            adminUser.setBalance(adminUser.getBalance().add(totalCommission));
            userRepository.save(adminUser);
        }

        for (Rental rental : confirmedRentals) {
            commissionService.calculateAndSaveCommission(rental);
        }
    }


}