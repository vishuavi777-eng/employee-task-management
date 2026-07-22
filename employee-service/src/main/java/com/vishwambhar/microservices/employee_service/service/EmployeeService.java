package com.vishwambhar.microservices.employee_service.service;

import com.vishwambhar.microservices.employee_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeCreateRequest request);

    EmployeeResponse getEmployeeById(Long employeeId);

    Page<EmployeeResponse> getAllEmployees(Pageable pageable);

    EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    );

    EmployeeResponse updateEmployeeStatus(
            Long employeeId,
            EmployeeStatusUpdateRequest request
    );

    void deactivateEmployee(Long employeeId);

    EmployeeValidationResponse validateEmployee(
            Long employeeId
    );
}