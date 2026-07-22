package com.vishwambhar.microservices.task_service.service.imp;

import com.vishwambhar.microservices.task_service.client.EmployeeClient;
import com.vishwambhar.microservices.task_service.dto.*;
import com.vishwambhar.microservices.task_service.entity.Task;
import com.vishwambhar.microservices.task_service.enums.TaskStatus;
import com.vishwambhar.microservices.task_service.exception.InvalidEmployeeException;
import com.vishwambhar.microservices.task_service.exception.TaskNotFoundException;
import com.vishwambhar.microservices.task_service.mapper.TaskMapper;
import com.vishwambhar.microservices.task_service.repository.TaskRepository;
import com.vishwambhar.microservices.task_service.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EmployeeClient employeeClient;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            TaskMapper taskMapper,
            EmployeeClient employeeClient
    ) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.employeeClient = employeeClient;
    }

    @Override
    @Transactional
    public TaskResponse createTask(
            TaskCreateRequest request
    ) {

        validateAssignedEmployee(request.assignedEmployeeId());

        Task task = taskMapper.toEntity(request);

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskResponse getTask(Long taskId) {

        Task task = getTaskEntity(taskId);

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskResponse> getAllTasks(
            Pageable pageable
    ) {

        return taskRepository
                .findAll(pageable)
                .map(taskMapper::toResponse);
    }

    @Override
    @Transactional
    public TaskResponse updateTask(
            Long taskId,
            TaskUpdateRequest request
    ) {

        Task task = getTaskEntity(taskId);

        validateAssignedEmployee(
                request.assignedEmployeeId()
        );

        taskMapper.updateEntity(task, request);

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse updateStatus(
            Long taskId,
            TaskStatusUpdateRequest request
    ) {

        Task task = getTaskEntity(taskId);

        task.changeStatus(request.status());

        return taskMapper.toResponse(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {

        Task task = getTaskEntity(taskId);

        /*
         * Soft delete:
         * We are not deleting the database row.
         */
        task.changeStatus(TaskStatus.CANCELLED);
    }


    /*
    * Helpers
    * */

    private Task getTaskEntity(Long taskId) {

        return taskRepository
                .findById(taskId)
                .orElseThrow(
                        () -> new TaskNotFoundException(taskId)
                );
    }

    private void validateAssignedEmployee(Long employeeId) {
        EmployeeValidationResponse employee = this.employeeClient.validateEmployee(employeeId);
        if (employee == null || !employee.exists()) {
            throw new InvalidEmployeeException(
                    "EMPLOYEE_NOT_FOUND",
                    "Employee does not exist with ID: "
                            + employeeId
            );
        }

        if (!employee.active()) {
            throw new InvalidEmployeeException(
                    "EMPLOYEE_INACTIVE",
                    "Task cannot be assigned to inactive employee: "
                            + employeeId
            );
        }
    }
}