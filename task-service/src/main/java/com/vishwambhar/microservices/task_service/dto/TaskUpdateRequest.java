package com.vishwambhar.microservices.task_service.dto;

import com.vishwambhar.microservices.task_service.enums.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskUpdateRequest(

        @NotBlank
        String title,

        String description,

        @NotNull
        Long assignedEmployeeId,

        @NotNull
        TaskPriority priority,

        @NotNull
        LocalDate dueDate
) {}