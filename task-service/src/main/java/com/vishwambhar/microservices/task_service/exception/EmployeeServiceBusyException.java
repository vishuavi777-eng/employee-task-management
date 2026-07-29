package com.vishwambhar.microservices.task_service.exception;

public class EmployeeServiceBusyException extends RuntimeException {

    private final String errorCode;

    public EmployeeServiceBusyException(String message) {
        super(message);
        this.errorCode = "EMPLOYEE_SERVICE_BUSY";
    }

    public EmployeeServiceBusyException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.errorCode = "EMPLOYEE_SERVICE_BUSY";
    }

    public String getErrorCode() {
        return errorCode;
    }
}