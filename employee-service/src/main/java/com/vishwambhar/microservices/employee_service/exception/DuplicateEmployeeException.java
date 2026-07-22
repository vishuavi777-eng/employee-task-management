package com.vishwambhar.microservices.employee_service.exception;

public class DuplicateEmployeeException extends RuntimeException {

    private final String errorCode;

    public DuplicateEmployeeException(
            String errorCode,
            String message
    ) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}