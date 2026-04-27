package com.banking.api.bakalaura_darbs_platform_threads.dto.payment;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.PaymentType;

import java.math.BigDecimal;

public record CreatePaymentRequest(
    Long senderAccountId,
    Long receiverAccountId,
    BigDecimal amount,
    String deduplicationKey,
    String receiverAccountIban,
    String receiverName,
    String receiverBic,
    String currency,
    String description,
    PaymentType paymentType,
    String metadata
) {}
