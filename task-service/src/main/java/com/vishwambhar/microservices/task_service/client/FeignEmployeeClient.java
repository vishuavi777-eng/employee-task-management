package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceRequestException;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class FeignEmployeeClient implements EmployeeClient {

    private static final String EMPLOYEE_SERVICE_CIRCUIT_BREAKER =
            "employeeService";

    private static final String EMPLOYEE_RETRY =
            "employeeServiceRetry";

    private final EmployeeFeignApi employeeFeignApi;

    public FeignEmployeeClient(
            EmployeeFeignApi employeeFeignApi
    ) {
        this.employeeFeignApi = employeeFeignApi;
    }

    @CircuitBreaker(
            name = EMPLOYEE_SERVICE_CIRCUIT_BREAKER,
            fallbackMethod = "validateEmployeeFallback"
    )
    @Retry(name = EMPLOYEE_RETRY)
    @Override
    public EmployeeValidationResponse validateEmployee(
            Long employeeId
    ) {

        try {
            EmployeeValidationResponse response =
                    employeeFeignApi.validateEmployee(employeeId);

            if (response == null) {
                throw new EmployeeServiceUnavailableException(
                        "Employee Service returned an empty response"
                );
            }

            return response;

        } catch (RetryableException exception) {

            /*
             * Usually includes:
             *
             * - Connection refused
             * - Connect timeout
             * - Read timeout
             * - Temporary network failure
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service is unavailable or did not respond in time",
                    exception
            );

        } catch (FeignException.FeignServerException exception) {

            /*
             * Employee Service returned HTTP 5xx.
             */
            throw new EmployeeServiceUnavailableException(
                    "Employee Service returned server error: "
                            + exception.status(),
                    exception
            );

        } catch (FeignException.FeignClientException exception) {

            // HTTP 4xx: not retryable.
            throw new EmployeeServiceRequestException(
                    "Employee Service rejected the request with status: "
                            + exception.status(),
                    exception
            );

        } catch (FeignException exception) {

            throw new EmployeeServiceUnavailableException(
                    "Failed to communicate with Employee Service",
                    exception
            );
        }
    }

    public EmployeeValidationResponse validateEmployeeFallback(Long employeeId, Throwable throwable) {
        if (throwable instanceof CallNotPermittedException) {
            throw new EmployeeServiceUnavailableException(
                    "Employee Service is temporarily unavailable because "
                            + "the circuit breaker is OPEN",
                    throwable
            );
        }

        if (throwable instanceof EmployeeServiceRequestException exception) {
            throw exception;
        }

        if (throwable instanceof EmployeeServiceUnavailableException exception) {
            throw exception;
        }

        throw new EmployeeServiceUnavailableException(
                "Employee Service validation failed for employee ID: "
                        + employeeId,
                throwable
        );
    }
}