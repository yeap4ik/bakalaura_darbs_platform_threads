package com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.dao;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Payment;

import java.util.UUID;

public interface CustomPaymentDao {
    Payment findPaymentById(UUID id);
}
