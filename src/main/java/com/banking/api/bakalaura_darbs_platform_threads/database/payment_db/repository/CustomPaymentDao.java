package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.repository;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPaymentDao {
    Payment findPaymentById(UUID id);

    Page<Payment> findPayments(Pageable pageable);
}
