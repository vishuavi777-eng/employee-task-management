package com.vishwambhar.microservices.employee_service.mapper;

import com.vishwambhar.microservices.employee_service.dto.EmployeeCreateRequest;
import com.vishwambhar.microservices.employee_service.dto.EmployeeResponse;
import com.vishwambhar.microservices.employee_service.dto.EmployeeUpdateRequest;
import com.vishwambhar.microservices.employee_service.entity.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public Employee toEntity(EmployeeCreateRequest request) {

        return Employee.builder()
                .employeeCode(
                        normalizeEmployeeCode(
                                request.employeeCode()
                        )
                )
                .firstName(
                        normalizeName(request.firstName())
                )
                .lastName(
                        normalizeName(request.lastName())
                )
                .email(
                        normalizeEmail(request.email())
                )
                .department(request.department())
                .designation(request.designation().trim())
                .build();
    }

    public void updateEntity(
            Employee employee,
            EmployeeUpdateRequest request
    ) {

        employee.updateDetails(
                normalizeName(request.firstName()),
                normalizeName(request.lastName()),
                normalizeEmail(request.email()),
                request.department(),
                request.designation().trim()
        );
    }

    public EmployeeResponse toResponse(Employee employee) {

        String fullName =
                employee.getFirstName()
                        + " "
                        + employee.getLastName();

        return new EmployeeResponse(
                employee.getId(),
                employee.getEmployeeCode(),
                employee.getFirstName(),
                employee.getLastName(),
                fullName,
                employee.getEmail(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getStatus(),
                employee.getCreatedAt(),
                employee.getUpdatedAt()
        );
    }

    private String normalizeEmployeeCode(
            String employeeCode
    ) {
        return employeeCode.trim().toUpperCase();
    }

    public String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private String normalizeName(String name) {

        String trimmedName = name.trim();

        if (trimmedName.isEmpty()) {
            return trimmedName;
        }

        return Character.toUpperCase(
                trimmedName.charAt(0)
        ) + trimmedName.substring(1);
    }
}