package com.banking.api.bakalaura_darbs_platform_threads.dto.external;

import com.banking.api.bakalaura_darbs_platform_threads.database.payment_db.entity.User;

import java.math.BigDecimal;

public record LoyaltyRewardRequest(User user, BigDecimal amount) {
}
