package com.banking.api.bakalaura_darbs_platform_threads.service.integration;

import com.banking.api.bakalaura_darbs_platform_threads.config.RestTemplateConfig;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyServiceClient {

    private final ExternalServiceClient externalClient;
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LoyaltyServiceClient.class);

    public LoyaltyServiceClient(@Qualifier("externalServiceClient") ExternalServiceClient externalClient) {
        this.externalClient = externalClient;
    }

    public LoyaltyRewardResponse reward(LoyaltyRewardRequest request) {
//        log.info("Processing reward call, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        return externalClient.post(
                "/api/external/loyalty/reward",
                request,
                LoyaltyRewardResponse.class,
                "Loyalty service call failed"
        );
    }
}
