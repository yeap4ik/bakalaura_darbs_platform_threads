package com.banking.api.bakalaura_darbs_platform_threads.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "paymentParallelExecutor")
    public Executor paymentParallelExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Default core pool size:
        executor.setCorePoolSize(50);

        // Maximum pool size: (+200 MB RAM)
        executor.setMaxPoolSize(200);

        //Maximum queue capacity. If 200 threads are busy, 500 tasks will wait in the queue.
        executor.setQueueCapacity(500);

        executor.setThreadNamePrefix("Payment-Platform-");

        executor.initialize();
        return executor;
    }
}
