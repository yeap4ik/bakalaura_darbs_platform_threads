package com.banking.api.bakalaura_darbs_platform_threads.service.integration;

import com.banking.api.bakalaura_darbs_platform_threads.dto.external.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class BankServiceClient {
    private final ExternalServiceClient externalClient;
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BankServiceClient.class);

    public BankServiceClient(@Qualifier("externalServiceClient") ExternalServiceClient externalClient) {
        this.externalClient = externalClient;
    }
    public BankTransferResponse transfer(BankTransferRequest request) {
//        log.info("Processing bank transfer, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return externalClient.post(
                "/api/external/bank/transfer",
                request,
                BankTransferResponse.class,
                "Bank service call failed"
        );
    }
}
