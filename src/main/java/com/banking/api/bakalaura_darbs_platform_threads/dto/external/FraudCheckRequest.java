package com.banking.api.bakalaura_darbs_platform_threads.dto.external;

import java.math.BigDecimal;

public record FraudCheckRequest(Long userId, BigDecimal amount) {
}
