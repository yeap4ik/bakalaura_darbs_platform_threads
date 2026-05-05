package com.banking.api.bakalaura_darbs_platform_threads.dto.external;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.Account;

import java.math.BigDecimal;
import java.util.UUID;

public record BankTransferRequest(Account sender, Account receiver, BigDecimal amount, UUID paymentId) {
}
