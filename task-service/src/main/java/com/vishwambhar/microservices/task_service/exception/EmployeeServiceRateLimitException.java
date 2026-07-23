package com.vishwambhar.microservices.task_service.exception;

public class EmployeeServiceRateLimitException extends RuntimeException {
  private final String errorCode;

  public EmployeeServiceRateLimitException(
          String message
  ) {
    super(message);
    this.errorCode = "EMPLOYEE_SERVICE_REQUEST_FAILED";
  }

  public EmployeeServiceRateLimitException(
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
