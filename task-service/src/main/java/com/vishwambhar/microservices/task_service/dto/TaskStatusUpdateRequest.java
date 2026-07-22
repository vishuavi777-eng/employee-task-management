package com.vishwambhar.microservices.task_service.dto;

import com.vishwambhar.microservices.task_service.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(

        @NotNull
        TaskStatus status

) {}