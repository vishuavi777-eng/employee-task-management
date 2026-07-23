package com.vishwambhar.microservices.task_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncExecutorConfig {

    @Bean(name = "employeeServiceExecutor")
    public Executor employeeServiceExecutor() {
        return Executors.newFixedThreadPool(10);
    }
}
