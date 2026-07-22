package com.vishwambhar.microservices.task_service.controller;

import com.vishwambhar.microservices.task_service.dto.TaskCreateRequest;
import com.vishwambhar.microservices.task_service.dto.TaskResponse;
import com.vishwambhar.microservices.task_service.dto.TaskStatusUpdateRequest;
import com.vishwambhar.microservices.task_service.dto.TaskUpdateRequest;
import com.vishwambhar.microservices.task_service.service.TaskService;
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
@RequestMapping("/api/v1/tasks")
@Validated
public class TaskController {

    private static final int MAX_PAGE_SIZE = 100;

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid
            @RequestBody
            TaskCreateRequest request
    ) {

        TaskResponse task =
                taskService.createTask(request);

        URI location = URI.create(
                "/api/v1/tasks/" + task.id()
        );

        return ResponseEntity
                .created(location)
                .body(task);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable
            @Min(
                    value = 1,
                    message = "Task ID must be greater than zero"
            )
            Long taskId
    ) {

        TaskResponse task =
                taskService.getTask(taskId);

        return ResponseEntity.ok(task);
    }

    @GetMapping
    public ResponseEntity<Page<TaskResponse>> getAllTasks(

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page number cannot be negative"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Page size must be greater than zero"
            )
            int size,

            @RequestParam(defaultValue = "id")
            String sortBy,

            @RequestParam(defaultValue = "asc")
            String direction
    ) {

        int validatedSize =
                Math.min(size, MAX_PAGE_SIZE);

        String validatedSortField =
                validateSortField(sortBy);

        Sort.Direction sortDirection =
                direction.equalsIgnoreCase("desc")
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(
                page,
                validatedSize,
                Sort.by(
                        sortDirection,
                        validatedSortField
                )
        );

        Page<TaskResponse> tasks =
                taskService.getAllTasks(pageable);

        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(

            @PathVariable
            @Min(
                    value = 1,
                    message = "Task ID must be greater than zero"
            )
            Long taskId,

            @Valid
            @RequestBody
            TaskUpdateRequest request
    ) {

        TaskResponse task =
                taskService.updateTask(
                        taskId,
                        request
                );

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(

            @PathVariable
            @Min(
                    value = 1,
                    message = "Task ID must be greater than zero"
            )
            Long taskId,

            @Valid
            @RequestBody
            TaskStatusUpdateRequest request
    ) {

        TaskResponse task =
                taskService.updateStatus(
                        taskId,
                        request
                );

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(

            @PathVariable
            @Min(
                    value = 1,
                    message = "Task ID must be greater than zero"
            )
            Long taskId
    ) {

        taskService.deleteTask(taskId);

        return ResponseEntity
                .noContent()
                .build();
    }

    private String validateSortField(String sortBy) {

        return switch (sortBy) {
            case "id",
                 "title",
                 "assignedEmployeeId",
                 "priority",
                 "status",
                 "dueDate",
                 "createdAt",
                 "updatedAt" -> sortBy;

            default -> "id";
        };
    }
}