package com.banking.api.bakalaura_darbs_platform_threads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.net.http.HttpClient;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestClient externalServicesRestClient(ExternalServicesProperties properties) {
        HttpClient jdkHttpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofMillis(properties.getConnectionTimeout()))
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(jdkHttpClient);
        requestFactory.setReadTimeout(Duration.ofMillis(properties.getReadTimeout()));

        return RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(properties.getBaseUrl())
                .defaultHeaders(headers -> headers.setBasicAuth(
                        properties.getLogin(),
                        properties.getPassword()
                ))
                .build();
    }

//    public RestClient.RequestBodyUriSpec post() {
//        return externalServicesRestClient.post();
//    }
}

//    @Bean(destroyMethod = "shutdown")
//    public ExecutorService paymentTaskExecutor() {
//        int configuredThreadCount = externalServicesProperties.getThreadCount();
//        int threadCount = configuredThreadCount > 0
//                ? configuredThreadCount
//                : Math.max(4, Runtime.getRuntime().availableProcessors());
//
//        return Executors.newFixedThreadPool(threadCount);
//    }

