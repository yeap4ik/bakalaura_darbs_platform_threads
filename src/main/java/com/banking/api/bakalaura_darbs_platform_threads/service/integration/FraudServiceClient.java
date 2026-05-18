package com.banking.api.bakalaura_darbs_platform_threads.service.integration;

import com.banking.api.bakalaura_darbs_platform_threads.dto.external.FraudCheckRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.FraudCheckResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class FraudServiceClient {

    private final ExternalServiceClient externalClient;
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(FraudServiceClient.class);

    public FraudServiceClient(@Qualifier("externalServiceClient") ExternalServiceClient externalClient) {
        this.externalClient = externalClient;
    }

    public FraudCheckResponse checkFraud(FraudCheckRequest request) {
//        log.info("Processing fraud check, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return externalClient.post(
                "/api/external/fraud/check",
                request,
                FraudCheckResponse.class,
                "Fraud service call failed"
        );
    }
}
