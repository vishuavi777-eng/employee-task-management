package com.vishwambhar.microservices.employee_service.service.impl;

import com.vishwambhar.microservices.employee_service.dto.*;
import com.vishwambhar.microservices.employee_service.entity.Employee;
import com.vishwambhar.microservices.employee_service.enums.EmployeeStatus;
import com.vishwambhar.microservices.employee_service.exception.DuplicateEmployeeException;
import com.vishwambhar.microservices.employee_service.exception.EmployeeNotFoundException;
import com.vishwambhar.microservices.employee_service.mapper.EmployeeMapper;
import com.vishwambhar.microservices.employee_service.repository.EmployeeRepository;
import com.vishwambhar.microservices.employee_service.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeMapper employeeMapper
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public EmployeeResponse createEmployee(
            EmployeeCreateRequest request
    ) {

        String normalizedEmployeeCode =
                request.employeeCode().trim().toUpperCase();

        String normalizedEmail =
                request.email().trim().toLowerCase();

        validateEmployeeCodeIsUnique(normalizedEmployeeCode);
        validateEmailIsUnique(normalizedEmail);

        Employee employee = employeeMapper.toEntity(request);

        Employee savedEmployee =
                employeeRepository.save(employee);

        return employeeMapper.toResponse(savedEmployee);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(employeeId)
                );

        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeResponse> getAllEmployees(
            Pageable pageable
    ) {

        return employeeRepository
                .findAll(pageable)
                .map(employeeMapper::toResponse);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(
            Long employeeId,
            EmployeeUpdateRequest request
    ) {

        Employee employee = getEmployeeEntity(employeeId);

        String normalizedEmail =
                employeeMapper.normalizeEmail(request.email());

        validateEmailIsUniqueForUpdate(
                normalizedEmail,
                employeeId
        );

        employeeMapper.updateEntity(employee, request);

        return employeeMapper.toResponse(employee);
    }


    @Override
    @Transactional
    public EmployeeResponse updateEmployeeStatus(
            Long employeeId,
            EmployeeStatusUpdateRequest request
    ) {
        Employee employee = getEmployeeEntity(employeeId);

        employee.changeStatus(request.status());

        return employeeMapper.toResponse(employee);
    }

    @Override
    @Transactional
    public void deactivateEmployee(Long employeeId) {

        Employee employee = getEmployeeEntity(employeeId);

        employee.changeStatus(EmployeeStatus.INACTIVE);
    }

    private void validateEmployeeCodeIsUnique(
            String employeeCode
    ) {

        boolean employeeCodeExists =
                employeeRepository
                        .existsByEmployeeCodeIgnoreCase(employeeCode);

        if (employeeCodeExists) {
            throw new DuplicateEmployeeException(
                    "DUPLICATE_EMPLOYEE_CODE",
                    "Employee code already exists: " + employeeCode
            );
        }
    }

    private void validateEmailIsUnique(String email) {

        boolean emailExists =
                employeeRepository.existsByEmailIgnoreCase(email);

        if (emailExists) {
            throw new DuplicateEmployeeException(
                    "DUPLICATE_EMPLOYEE_EMAIL",
                    "Employee email already exists: " + email
            );
        }
    }

    private Employee getEmployeeEntity(Long employeeId) {

        return employeeRepository
                .findById(employeeId)
                .orElseThrow(
                        () -> new EmployeeNotFoundException(employeeId)
                );
    }

    private void validateEmailIsUniqueForUpdate(
            String email,
            Long employeeId
    ) {

        boolean emailExists =
                employeeRepository
                        .existsByEmailIgnoreCaseAndIdNot(
                                email,
                                employeeId
                        );

        if (emailExists) {
            throw new DuplicateEmployeeException(
                    "DUPLICATE_EMPLOYEE_EMAIL",
                    "Employee email already exists: " + email
            );
        }
    }


    @Override
    @Transactional(readOnly = true)
    public EmployeeValidationResponse validateEmployee(
            Long employeeId
    ) {
        log.info(
                "Validating employee with ID: {}",
                employeeId
        );
        return employeeRepository
                .findById(employeeId)
                .map(employee -> {

                    boolean active =
                            employee.getStatus()
                                    == EmployeeStatus.ACTIVE;

                    String fullName =
                            employee.getFirstName()
                                    + " "
                                    + employee.getLastName();

                    return new EmployeeValidationResponse(
                            employee.getId(),
                            true,
                            active,
                            employee.getEmployeeCode(),
                            fullName
                    );
                })
                .orElseGet(
                        () -> EmployeeValidationResponse
                                .notFound(employeeId)
                );
    }
}