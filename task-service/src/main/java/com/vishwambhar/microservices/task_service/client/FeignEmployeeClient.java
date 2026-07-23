package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceBusyException;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceRequestException;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;

@Component
public class FeignEmployeeClient implements EmployeeClient {

    private static final String EMPLOYEE_SERVICE_CIRCUIT_BREAKER =
            "employeeService";

    private static final String EMPLOYEE_RETRY =
            "employeeServiceRetry";

    private static final String EMPLOYEE_BULKHEAD =
            "employeeServiceBulkhead";

    private static final String EMPLOYEE_RATE_LIMITER =
            "employeeServiceRateLimiter";

    private final EmployeeFeignApi employeeFeignApi;

    public FeignEmployeeClient(
            EmployeeFeignApi employeeFeignApi
    ) {
        this.employeeFeignApi = employeeFeignApi;
    }

    @Bulkhead(
            name = EMPLOYEE_BULKHEAD,
            fallbackMethod = "validateEmployeeFallback"
    )
    @CircuitBreaker(
            name = EMPLOYEE_SERVICE_CIRCUIT_BREAKER,
            fallbackMethod = "validateEmployeeFallback"
    )
    @Retry(name = EMPLOYEE_RETRY)
    @RateLimiter(name = EMPLOYEE_RATE_LIMITER)
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

            throw new EmployeeServiceUnavailableException(
                    "Employee Service is unavailable or did not respond in time",
                    exception
            );

        } catch (FeignException.FeignServerException exception) {

            throw new EmployeeServiceUnavailableException(
                    "Employee Service returned server error: "
                            + exception.status(),
                    exception
            );

        } catch (FeignException.FeignClientException exception) {

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

    public EmployeeValidationResponse validateEmployeeFallback(
            Long employeeId,
            Throwable throwable
    ) {
        Throwable actualException = unwrap(throwable);

        if (actualException instanceof RequestNotPermitted exception) {
            throw exception;
        }

        if (actualException instanceof BulkheadFullException) {
            throw new EmployeeServiceBusyException(
                    "Too many Employee Service validation requests are "
                            + "currently running. Please try again shortly.",
                    actualException
            );
        }

        if (actualException instanceof CallNotPermittedException) {
            throw new EmployeeServiceUnavailableException(
                    "Employee Service is temporarily unavailable because "
                            + "the circuit breaker is OPEN",
                    actualException
            );
        }

        if (actualException instanceof EmployeeServiceRequestException exception) {
            throw exception;
        }

        if (actualException instanceof EmployeeServiceBusyException exception) {
            throw exception;
        }

        if (actualException
                instanceof EmployeeServiceUnavailableException exception) {
            throw exception;
        }

        throw new EmployeeServiceUnavailableException(
                "Employee Service validation failed for employee ID: "
                        + employeeId,
                actualException
        );
    }

    private Throwable unwrap(Throwable throwable) {
        Throwable current = throwable;

        while (current.getCause() != null
                && (current instanceof java.util.concurrent.CompletionException
                || current instanceof java.util.concurrent.ExecutionException)) {
            current = current.getCause();
        }

        return current;
    }
}