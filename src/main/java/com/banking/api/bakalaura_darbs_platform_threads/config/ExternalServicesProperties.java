package com.banking.api.bakalaura_darbs_platform_threads.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external-services")
@Data
public class ExternalServicesProperties {
    private String baseUrl;
    private String login;
    private String password;

}
