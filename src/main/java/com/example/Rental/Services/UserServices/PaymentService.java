package com.example.Rental.Services.UserServices;

import com.example.Rental.models.Entity.Payment;
import com.example.Rental.models.Entity.Rental;
import com.example.Rental.models.Enumes.PaymentMethod;
import com.example.Rental.models.Enumes.PaymentStatus;
import com.example.Rental.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    public PaymentService(PaymentRepository paymentRepository){
        this.paymentRepository=paymentRepository;
    }
    // معالجة الدفعة الأولية عند تأكيد الطلب
    public void processInitialPayment(Rental rental, PaymentMethod method) {
        BigDecimal initialAmount = rental.getTotalPrice().multiply(new BigDecimal("0.70")); // مثال: 50% دفعة أولية
        Payment initialPayment = new Payment();
        initialPayment.setRental(rental);
        initialPayment.setAmount(initialAmount.doubleValue());
        initialPayment.setPaymentMethod(method);
        initialPayment.setStatus(PaymentStatus.PARTIALLY_PAID);
        rental.setInitialPaymentAmount(initialAmount);

        // احفظ معلومات الدفعة في قاعدة البيانات
        paymentRepository.save(initialPayment);
    }

    // معالجة الدفعة النهائية عند استلام الطلب
    public void processFinalPayment(Rental rental, PaymentMethod method) {
        BigDecimal remainingAmount = rental.getTotalPrice().subtract(rental.getInitialPaymentAmount());
        Payment finalPayment = new Payment();
        finalPayment.setRental(rental);
        finalPayment.setAmount(remainingAmount.doubleValue());
        finalPayment.setPaymentMethod(method);
        finalPayment.setStatus(PaymentStatus.FULLY_PAID);
        rental.setRemainingPaymentAmount(BigDecimal.ZERO);

        // تحديث حالة الدفع إلى دفع كامل
        paymentRepository.save(finalPayment);
    }
}
