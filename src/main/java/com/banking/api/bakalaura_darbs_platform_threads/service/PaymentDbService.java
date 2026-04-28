package com.banking.api.bakalaura_darbs_platform_threads.service;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.PaymentStatus;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.PaymentRepository;
import com.banking.api.bakalaura_darbs_platform_threads.dto.payment.CreatePaymentRequest;
import com.banking.api.bakalaura_darbs_platform_threads.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentDbService {

    private final PaymentRepository paymentRepository;

    public PaymentDbService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPendingPayment(CreatePaymentRequest request, Account senderAccount, Account receiverAccount) {
        Payment payment = Payment.createPending(senderAccount, receiverAccount, request);
        return paymentRepository.save(payment);
    }

    @Transactional
    public void rejectPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        payment.setStatus(PaymentStatus.REJECTED);
        paymentRepository.save(payment);
    }

    @Transactional
    public Payment markPaymentSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment);
    }
}
