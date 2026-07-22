package com.vishwambhar.microservices.task_service.exception;

public class EmployeeServiceUnavailableException
        extends RuntimeException {

    private final String errorCode;

    public EmployeeServiceUnavailableException(
            String message
    ) {
        super(message);
        this.errorCode = "EMPLOYEE_SERVICE_UNAVAILABLE";
    }

    public EmployeeServiceUnavailableException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.errorCode = "EMPLOYEE_SERVICE_UNAVAILABLE";
    }

    public String getErrorCode() {
        return errorCode;
    }
}