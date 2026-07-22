package com.vishwambhar.microservices.task_service.client;

import com.vishwambhar.microservices.task_service.dto.EmployeeValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "employee-service",
        url = "${employee.service.base-url}"
)
public interface EmployeeFeignApi {
    @GetMapping("/api/v1/employees/{employeeId}/validation")
    EmployeeValidationResponse validateEmployee(@PathVariable Long employeeId);
}
