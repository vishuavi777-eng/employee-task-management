package com.vishwambhar.microservices.employee_service.dto;

import com.vishwambhar.microservices.employee_service.enums.Department;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EmployeeCreateRequest(

        @NotBlank(message = "Employee code is required")
        @Size(
                min = 3,
                max = 30,
                message = "Employee code must contain between 3 and 30 characters"
        )
        String employeeCode,

        @NotBlank(message = "First name is required")
        @Size(
                max = 100,
                message = "First name cannot exceed 100 characters"
        )
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(
                max = 100,
                message = "Last name cannot exceed 100 characters"
        )
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(
                max = 150,
                message = "Email cannot exceed 150 characters"
        )
        String email,

        @NotNull(message = "Department is required")
        Department department,

        @NotBlank(message = "Designation is required")
        @Size(
                max = 100,
                message = "Designation cannot exceed 100 characters"
        )
        String designation
) {
}