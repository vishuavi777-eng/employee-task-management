package com.vishwambhar.microservices.task_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

//@Configuration
public class RestClientConfig {

    @Bean
    public RestClient.Builder restClientBuilder() {

        return RestClient.builder();
        
    }

    @Bean
    public RestClient employeeRestClient(
            RestClient.Builder builder,
            @Value("${employee.service.base-url}")
            String employeeServiceBaseUrl,
            @Value("${employee.service.connect-timeout}")
            Duration connectTimeout,
            @Value("${employee.service.read-timeout}")
            Duration readTimeout
    ) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        requestFactory.setConnectTimeout(connectTimeout);

        requestFactory.setReadTimeout(readTimeout);

        return builder
                .baseUrl(employeeServiceBaseUrl)
                .requestFactory(requestFactory)
                .build();
    }
}