package com.banking.api.bakalaura_darbs_platform_threads.service.integration;

import com.banking.api.bakalaura_darbs_platform_threads.exception.ExternalServiceException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class ExternalServiceClient {
    private final RestClient restClient;
//    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ExternalServiceClient.class);

    public ExternalServiceClient(RestClient externalServicesRestClient) {
        this.restClient = externalServicesRestClient;
    }

    public <T> T post(String uri, Object requestBody, Class<T> responseType, String errorMessage) {
//        log.info("Processing post http call, thread name={} (virtual={})", Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        try {
            T response = restClient.post()
                    .uri(uri)
                    .body(requestBody)
                    .retrieve()
                    .body(responseType);

            if (response == null) {
                throw new ExternalServiceException(errorMessage + ": empty response body");
            }
            return response;
        } catch (RestClientException ex) {
            throw new ExternalServiceException(errorMessage, ex);
        }
    }
}
