package com.vishwambhar.microservices.employee_service.dto;

import com.vishwambhar.microservices.employee_service.enums.Department;
import com.vishwambhar.microservices.employee_service.enums.EmployeeStatus;

import java.time.LocalDateTime;

public record EmployeeResponse(

        Long id,

        String employeeCode,

        String firstName,

        String lastName,

        String fullName,

        String email,

        Department department,

        String designation,

        EmployeeStatus status,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}