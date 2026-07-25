package com.vishwambhar.microservices.task_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvironmentController {

    @Value("${app.environment-name}")
    private String environmentName;

    @GetMapping("/api/v1/environment")
    public String getEnvironment() {
        return environmentName;
    }
}