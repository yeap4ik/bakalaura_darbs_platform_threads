package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;
import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository.PaymentRepository;
import com.banking.api.bakalaura_darbs_platform_threads.exception.ResourceNotFoundException;

import java.util.UUID;

public class PaymentDaoImpl implements CustomPaymentDao {

    private final PaymentRepository paymentRepository;
    public PaymentDaoImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findPaymentById(UUID id){
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }
}
