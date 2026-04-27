package com.banking.api.bakalaura_darbs_platform_threads.service.integration;

import com.banking.api.bakalaura_darbs_platform_threads.config.RestTemplateConfig;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardRequest;
import com.banking.api.bakalaura_darbs_platform_threads.dto.external.LoyaltyRewardResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class LoyaltyServiceClient {

    private final ExternalServiceClient externalClient;

    public LoyaltyServiceClient(@Qualifier("externalServiceClient") ExternalServiceClient externalClient) {
        this.externalClient = externalClient;
    }

    public LoyaltyRewardResponse reward(LoyaltyRewardRequest request) {
        return externalClient.post(
                "/api/external/loyalty/reward",
                request,
                LoyaltyRewardResponse.class,
                "Loyalty service call failed"
        );
    }
}
