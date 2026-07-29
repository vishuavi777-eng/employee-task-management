package com.vishwambhar.microservices.task_service.exception;

public class EmployeeServiceRequestException extends RuntimeException {

    private final String errorCode;

    public EmployeeServiceRequestException(
            String message
    ) {
        super(message);
        this.errorCode = "EMPLOYEE_SERVICE_REQUEST_FAILED";
    }

    public EmployeeServiceRequestException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.errorCode = "EMPLOYEE_SERVICE_REQUEST_FAILED";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
