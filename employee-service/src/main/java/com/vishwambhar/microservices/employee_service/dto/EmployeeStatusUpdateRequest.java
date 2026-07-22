package com.vishwambhar.microservices.employee_service.dto;

import com.vishwambhar.microservices.employee_service.enums.EmployeeStatus;
import jakarta.validation.constraints.NotNull;

public record EmployeeStatusUpdateRequest(

        @NotNull(message = "Employee status is required")
        EmployeeStatus status
) {
}