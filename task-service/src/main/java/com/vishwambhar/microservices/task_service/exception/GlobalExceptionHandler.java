package com.vishwambhar.microservices.task_service.exception;

import com.vishwambhar.microservices.task_service.logging.CorrelationIdConstants;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ApiErrorResponse>
    handleEmployeeNotFoundException(
            TaskNotFoundException exception,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "TASK_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(InvalidEmployeeException.class)
    public ResponseEntity<ApiErrorResponse>
    handleInvalidEmployeeException(
            InvalidEmployeeException exception,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }


    @ExceptionHandler(EmployeeServiceUnavailableException.class)
    public ResponseEntity<ApiErrorResponse>
    handleEmployeeServiceUnavailableException(
            EmployeeServiceUnavailableException exception,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse>
    handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {

        Map<String, String> validationErrors =
                new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError ->
                        validationErrors.putIfAbsent(
                                fieldError.getField(),
                                fieldError.getDefaultMessage()
                        )
                );

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "VALIDATION_FAILED",
                "Request validation failed",
                request.getRequestURI(),
                validationErrors,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(EmployeeServiceRequestException.class)
    public ResponseEntity<ApiErrorResponse>
    handleEmployeeServiceRequestException(
            EmployeeServiceRequestException exception,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.BAD_GATEWAY;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(EmployeeServiceBusyException.class)
    public ResponseEntity<ApiErrorResponse>
    handleEmployeeServiceBusyException(
            EmployeeServiceBusyException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .header(HttpHeaders.RETRY_AFTER, "2")
                .body(response);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimitExceeded(
            RequestNotPermitted exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "EMPLOYEE_SERVICE_RATE_LIMIT_EXCEEDED",
                "Too many Employee Service validation requests. "
                        + "Please try again shortly.",
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .header(HttpHeaders.RETRY_AFTER, "10")
                .body(response);
    }

    @ExceptionHandler(
            jakarta.validation.ConstraintViolationException.class
    )
    public ResponseEntity<ApiErrorResponse>
    handleConstraintViolationException(

            jakarta.validation.ConstraintViolationException exception,
            HttpServletRequest request
    ) {

        Map<String, String> validationErrors =
                new LinkedHashMap<>();

        exception.getConstraintViolations()
                .forEach(violation -> {

                    String propertyPath =
                            violation.getPropertyPath()
                                    .toString();

                    String fieldName =
                            propertyPath.substring(
                                    propertyPath.lastIndexOf('.') + 1
                            );

                    validationErrors.put(
                            fieldName,
                            violation.getMessage()
                    );
                });

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiErrorResponse response =
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        "CONSTRAINT_VIOLATION",
                        "Request parameter validation failed",
                        request.getRequestURI(),
                        validationErrors,
                        MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
                );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse>
    handleDataIntegrityViolationException(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {

        HttpStatus status = HttpStatus.CONFLICT;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "DATA_INTEGRITY_VIOLATION",
                "The supplied employee data conflicts with existing data",
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse>
    handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {

        HttpStatus status =
                HttpStatus.INTERNAL_SERVER_ERROR;

        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred",
                request.getRequestURI(),
                null,
                MDC.get(CorrelationIdConstants.CORRELATION_ID_MDC_KEY)
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }
}