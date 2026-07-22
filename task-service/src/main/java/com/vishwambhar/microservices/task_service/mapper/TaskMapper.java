package com.vishwambhar.microservices.task_service.mapper;

import com.vishwambhar.microservices.task_service.dto.TaskCreateRequest;
import com.vishwambhar.microservices.task_service.dto.TaskResponse;
import com.vishwambhar.microservices.task_service.dto.TaskUpdateRequest;
import com.vishwambhar.microservices.task_service.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public Task toEntity(TaskCreateRequest request){

        return Task.builder()
                .title(request.title().trim())
                .description(request.description())
                .assignedEmployeeId(request.assignedEmployeeId())
                .priority(request.priority())
                .dueDate(request.dueDate())
                .build();
    }

    public void updateEntity(
            Task task,
            TaskUpdateRequest request
    ){

        task.updateDetails(
                request.title().trim(),
                request.description(),
                request.assignedEmployeeId(),
                request.priority(),
                request.dueDate()
        );

    }

    public TaskResponse toResponse(Task task){

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getAssignedEmployeeId(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );

    }

}