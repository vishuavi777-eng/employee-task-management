package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceRequestException;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import feign.FeignException;
import feign.RetryableException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeoutException;

@Component
public class EmployeeResilienceExecutor {

    private static final String CIRCUIT_BREAKER =
            "employeeService";

    private static final String RETRY =
            "employeeServiceRetry";

    private static final String TIME_LIMITER =
            "employeeServiceTimeLimiter";

    private final EmployeeFeignApi employeeFeignApi;
    private final Executor employeeServiceExecutor;

    public EmployeeResilienceExecutor(
            EmployeeFeignApi employeeFeignApi,
            @Qualifier("employeeServiceExecutor")
            Executor employeeServiceExecutor
    ) {
        this.employeeFeignApi = employeeFeignApi;
        this.employeeServiceExecutor = employeeServiceExecutor;
    }

    @TimeLimiter(
            name = TIME_LIMITER,
            fallbackMethod = "validateEmployeeFallback"
    )
    @Retry(name = RETRY)
    @CircuitBreaker(name = CIRCUIT_BREAKER)
    public CompletableFuture<EmployeeValidationResponse> validateEmployee(
            Long employeeId
    ) {
        return CompletableFuture.supplyAsync(
                () -> callEmployeeService(employeeId),
                employeeServiceExecutor
        );
    }

    private EmployeeValidationResponse callEmployeeService(
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

    private CompletableFuture<EmployeeValidationResponse>
    validateEmployeeFallback(
            Long employeeId,
            Throwable throwable
    ) {
        Throwable actualException = unwrap(throwable);

        if (actualException instanceof TimeoutException) {
            return CompletableFuture.failedFuture(
                    new EmployeeServiceUnavailableException(
                            "Employee Service response exceeded the allowed time",
                            actualException
                    )
            );
        }

        if (actualException instanceof CallNotPermittedException) {
            return CompletableFuture.failedFuture(
                    new EmployeeServiceUnavailableException(
                            "Employee Service is temporarily unavailable because "
                                    + "the circuit breaker is OPEN",
                            actualException
                    )
            );
        }

        if (actualException instanceof EmployeeServiceRequestException exception) {
            return CompletableFuture.failedFuture(exception);
        }

        if (actualException instanceof EmployeeServiceUnavailableException exception) {
            return CompletableFuture.failedFuture(exception);
        }

        return CompletableFuture.failedFuture(
                new EmployeeServiceUnavailableException(
                        "Employee Service validation failed for employee ID: "
                                + employeeId,
                        actualException
                )
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
