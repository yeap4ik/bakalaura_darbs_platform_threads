package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;
import com.banking.api.bakalaura_darbs_platform_threads.exception.ResourceNotFoundException;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public class CustomPaymentDaoImpl implements CustomPaymentDao {

    private final PaymentRepository paymentRepository;
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CustomPaymentDaoImpl.class);

    public CustomPaymentDaoImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment findPaymentById(UUID id){
//        log.info("Processing db search findPaymentById, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    @Override
    public Slice<Payment> findPayments(Pageable pageable) {
//        log.info("Processing db search findPayments, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return paymentRepository.findBy(pageable);
    }
}
