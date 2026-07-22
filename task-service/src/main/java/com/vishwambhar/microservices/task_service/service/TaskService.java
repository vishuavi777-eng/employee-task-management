package com.vishwambhar.microservices.task_service.service;

import com.vishwambhar.microservices.task_service.dto.TaskCreateRequest;
import com.vishwambhar.microservices.task_service.dto.TaskResponse;
import com.vishwambhar.microservices.task_service.dto.TaskStatusUpdateRequest;
import com.vishwambhar.microservices.task_service.dto.TaskUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskResponse createTask(TaskCreateRequest request);

    TaskResponse getTask(Long id);

    Page<TaskResponse> getAllTasks(Pageable pageable);

    TaskResponse updateTask(Long id,
                            TaskUpdateRequest request);

    TaskResponse updateStatus(Long id,
                              TaskStatusUpdateRequest request);

    void deleteTask(Long id);
}
