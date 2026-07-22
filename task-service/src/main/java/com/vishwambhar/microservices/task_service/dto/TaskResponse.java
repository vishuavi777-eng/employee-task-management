package com.vishwambhar.microservices.task_service.dto;

import com.vishwambhar.microservices.task_service.enums.TaskPriority;
import com.vishwambhar.microservices.task_service.enums.TaskStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskResponse(

        Long id,

        String title,

        String description,

        Long assignedEmployeeId,

        TaskPriority priority,

        TaskStatus status,

        LocalDate dueDate,

        LocalDateTime createdAt,

        LocalDateTime updatedAt

) {}