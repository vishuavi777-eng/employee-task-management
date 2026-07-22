package com.vishwambhar.microservices.task_service.repository;

import com.vishwambhar.microservices.task_service.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository
        extends JpaRepository<Task, Long> {

}