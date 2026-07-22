package com.vishwambhar.microservices.task_service.exception;

public class InvalidEmployeeException
        extends RuntimeException {

    private final String errorCode;

    public InvalidEmployeeException(
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