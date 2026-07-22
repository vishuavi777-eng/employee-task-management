package com.vishwambhar.microservices.employee_service.dto;

public record EmployeeValidationResponse(

        Long employeeId,

        boolean exists,

        boolean active,

        String employeeCode,

        String fullName
) {

    public static EmployeeValidationResponse notFound(
            Long employeeId
    ) {
        return new EmployeeValidationResponse(
                employeeId,
                false,
                false,
                null,
                null
        );
    }
}