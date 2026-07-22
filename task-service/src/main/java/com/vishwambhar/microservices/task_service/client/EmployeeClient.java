package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;

public interface EmployeeClient {

    EmployeeValidationResponse validateEmployee(
            Long employeeId
    );
}