package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceRequestException;
import com.vishwambhar.microservices.task_service.exception.EmployeeServiceUnavailableException;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletionException;

@Component
public class FeignEmployeeClient implements EmployeeClient {

    private final EmployeeResilienceExecutor resilienceExecutor;

    public FeignEmployeeClient(
            EmployeeResilienceExecutor resilienceExecutor
    ) {
        this.resilienceExecutor = resilienceExecutor;
    }

    @Override
    public EmployeeValidationResponse validateEmployee(
            Long employeeId
    ) {
        try {
            return resilienceExecutor
                    .validateEmployee(employeeId)
                    .join();

        } catch (CompletionException exception) {
            Throwable actualException = exception.getCause();

            if (actualException instanceof EmployeeServiceRequestException requestException) {
                throw requestException;
            }

            if (actualException instanceof EmployeeServiceUnavailableException unavailableException) {
                throw unavailableException;
            }

            throw new EmployeeServiceUnavailableException(
                    "Employee Service validation failed",
                    actualException
            );
        }
    }
}