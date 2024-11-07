package com.example.Rental.Services.UserServices;

import com.example.Rental.DTO.PaymentRequest;
import com.example.Rental.Services.CommissionService;
import com.example.Rental.Services.InvoiceService;
import com.example.Rental.Services.PaymentProcessors.*;
import com.example.Rental.models.Entity.Delivery;
import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Entity.User;
import com.example.Rental.models.Enumes.DeliveryStatus;
import com.example.Rental.models.Enumes.PaymentStatus;
import com.example.Rental.models.Enumes.RentalStatus;
import com.example.Rental.models.Enumes.Role;
import com.example.Rental.repositories.DeliveryRepository;
import com.example.Rental.repositories.PaymentRepository;
import com.example.Rental.repositories.RentalRepository;
import com.example.Rental.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private RentalRepository rentalRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private CreditCardPaymentProcessor creditCardProcessor;
    @Autowired
    private PayPalPaymentProcessor payPalProcessor;
    @Autowired
    private StripePaymentProcessor stripeProcessor;
    @Autowired
    private BankTransferPaymentProcessor bankTransferProcessor;
    private final CommissionService commissionService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    public PaymentService(CommissionService commissionService) {
        this.commissionService = commissionService;
    }

    public void processPayment(PaymentRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Rental> confirmedRentals = rentalRepository.findByRenterAndStatus(user, RentalStatus.CONFIRMED);

        BigDecimal totalAmount = confirmedRentals.stream()
                .map(Rental::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(request.getDeliveryFee());

        if (user.getBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient funds for payment.");
        }

        user.setBalance(user.getBalance().subtract(totalAmount));
        userRepository.save(user);

        BigDecimal totalCommission = totalAmount.multiply(new BigDecimal("0.10"));
        BigDecimal totalOwnerAmount = BigDecimal.ZERO;

        boolean firstDelivery = true;

        for (Rental rental : confirmedRentals) {
            rental.setStatus(RentalStatus.DELIVERED);

            Payment payment = new Payment();
            payment.setRental(rental);
            payment.setAmount(rental.getTotalPrice());
            payment.setPaymentMethod(request.getPaymentMethod());

            payment.setDeliveryFee(request.getDeliveryFee().doubleValue());

            switch (request.getPaymentMethod()) {
                case CREDIT_CARD:
                    creditCardProcessor.processPayment(request, rental.getTotalPrice());
                    break;
                case PAYPAL:
                    payPalProcessor.processPayment(request, rental.getTotalPrice());
                    break;
                case STRIPE:
                    stripeProcessor.processPayment(request, rental.getTotalPrice());
                    break;
                case BANK_TRANSFER:
                    bankTransferProcessor.processPayment(request, rental.getTotalPrice());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + request.getPaymentMethod());
            }

            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            BigDecimal rentalAmount = rental.getTotalPrice();
            BigDecimal itemOwnerCommission = rentalAmount.multiply(new BigDecimal("0.90"));
            totalOwnerAmount = totalOwnerAmount.add(itemOwnerCommission);

            User itemOwner = rental.getItem().getUser();
            itemOwner.setBalance(itemOwner.getBalance().add(itemOwnerCommission));
            userRepository.save(itemOwner);



            Delivery delivery = new Delivery();
            delivery.setRental(rental);
            delivery.setDeliveryAddress(user.getContactInfo());
            delivery.setDeliveryDate(LocalDate.now().plusDays(3));
            delivery.setDeliveryStatus(DeliveryStatus.SHIPPED);
            delivery.setEstimatedDeliveryTime(LocalDateTime.now().plusDays(3));


            if (firstDelivery) {
                delivery.setDeliveryFee(request.getDeliveryFee());
                firstDelivery = false;
            } else {
                delivery.setDeliveryFee(BigDecimal.ZERO);
            }

            deliveryRepository.save(delivery);


            String message = "Your rental item has been shipped and will arrive on " + delivery.getEstimatedDeliveryTime();
            notificationService.sendNotification(user, "Delivery Status Update", message, rental, rental.getItem());


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

    public String getPaymentStatus(String transactionId) {
        return paymentRepository.findByTransactionId(transactionId)
                .map(Payment::getStatus)
                .orElseThrow(() -> new RuntimeException("Payment not found"))
                .name();
    }
    public void processPartialPayment(PaymentRequest request, BigDecimal partialAmount) {
        Rental rental = rentalRepository.findById(request.getRentalId())
                .orElseThrow(() -> new RuntimeException("Rental not found for the given ID"));

        User user = rental.getRenter();

        List<Payment> payments = paymentRepository.findByRentalId(rental.getId());
        if (payments.isEmpty()) {
            throw new RuntimeException("Payment not found for rental ID");
        }

        Payment payment = payments.get(0);

        payment.addPartialPayment(partialAmount);

        if (payment.getPaidAmount().compareTo(payment.getAmount()) >= 0) {
            payment.setStatus(PaymentStatus.COMPLETED);
        } else {
            payment.setStatus(PaymentStatus.PARTIALLY_PAID);
        }

        paymentRepository.save(payment);
    }

}

