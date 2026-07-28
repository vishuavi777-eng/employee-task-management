package com.vishwambhar.microservices.employee_service.exception;

import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public record ApiErrorResponse(

        LocalDateTime timestamp,

        int status,

        String error,

        String errorCode,

        String message,

        String path,

        Map<String, String> validationErrors,

        String correlationId
) {

}