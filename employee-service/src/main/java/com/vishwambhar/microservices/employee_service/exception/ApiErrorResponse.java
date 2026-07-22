package com.vishwambhar.microservices.employee_service.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ApiErrorResponse(

        LocalDateTime timestamp,

        int status,

        String error,

        String errorCode,

        String message,

        String path,

        Map<String, String> validationErrors
) {
}