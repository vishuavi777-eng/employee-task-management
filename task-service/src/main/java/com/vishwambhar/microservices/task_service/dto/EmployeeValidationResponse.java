package com.vishwambhar.microservices.task_service.dto;

public record EmployeeValidationResponse(

        Long employeeId,

        boolean exists,

        boolean active,

        String employeeCode,

        String fullName
) {
}