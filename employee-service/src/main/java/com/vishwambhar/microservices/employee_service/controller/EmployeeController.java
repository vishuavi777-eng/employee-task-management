package com.vishwambhar.microservices.employee_service.controller;

import com.vishwambhar.microservices.employee_service.dto.*;
import com.vishwambhar.microservices.employee_service.service.EmployeeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/employees")
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(
            EmployeeService employeeService
    ) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request
    ) {

        EmployeeResponse employee =
                employeeService.createEmployee(request);

        URI location = URI.create(
                "/api/v1/employees/" + employee.id()
        );

        return ResponseEntity
                .created(location)
                .body(employee);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long employeeId
    ) {

        EmployeeResponse employee =
                employeeService.getEmployeeById(employeeId);

        return ResponseEntity.ok(employee);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(
            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        Page<EmployeeResponse> employees =
                employeeService.getAllEmployees(pageable);

        return ResponseEntity.ok(employees);
    }

    @PutMapping("/{employeeId}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long employeeId,
            @Valid @RequestBody EmployeeUpdateRequest request
    ) {

        EmployeeResponse employee =
                employeeService.updateEmployee(
                        employeeId,
                        request
                );

        return ResponseEntity.ok(employee);
    }

    @PatchMapping("/{employeeId}/status")
    public ResponseEntity<EmployeeResponse>
    updateEmployeeStatus(
            @PathVariable Long employeeId,
            @Valid
            @RequestBody
            EmployeeStatusUpdateRequest request
    ) {

        EmployeeResponse employee =
                employeeService.updateEmployeeStatus(
                        employeeId,
                        request
                );

        return ResponseEntity.ok(employee);
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deactivateEmployee(
            @PathVariable Long employeeId
    ) {

        employeeService.deactivateEmployee(employeeId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{employeeId}/validation")
    public ResponseEntity<EmployeeValidationResponse>
    validateEmployee(
            @Min(value = 1, message = "Employee ID must be greater than zero")
            @PathVariable Long employeeId
    ) throws InterruptedException {
        Thread.sleep(3000);

        EmployeeValidationResponse response =
                employeeService.validateEmployee(employeeId);

        return ResponseEntity.ok(response);
    }
}