package com.banking.api.bakalaura_darbs_platform_threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class BakalauraDarbsPlatformThreadsApplication {

    private static final Logger log = LoggerFactory.getLogger(BakalauraDarbsPlatformThreadsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BakalauraDarbsPlatformThreadsApplication.class, args);
    }

    @Bean
    ApplicationRunner startupLogger(Environment env) {
        return args -> {
            log.info("Active profiles: {}", Arrays.toString(env.getActiveProfiles()));
            log.info("Virtual threads enabled: {}", env.getProperty("spring.threads.virtual.enabled"));
            log.info("Startup thread: {} (virtual={})",
                    Thread.currentThread().getName(), Thread.currentThread().isVirtual());
        };

    }
}
