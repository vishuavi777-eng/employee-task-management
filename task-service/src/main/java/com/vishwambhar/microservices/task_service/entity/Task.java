package com.vishwambhar.microservices.task_service.entity;

import com.vishwambhar.microservices.task_service.enums.TaskPriority;
import com.vishwambhar.microservices.task_service.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private Long assignedEmployeeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;

        if(status == null){
            status = TaskStatus.OPEN;
        }
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = LocalDateTime.now();
    }

    public void updateDetails(
            String title,
            String description,
            Long assignedEmployeeId,
            TaskPriority priority,
            LocalDate dueDate
    ){

        this.title = title;
        this.description = description;
        this.assignedEmployeeId = assignedEmployeeId;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public void changeStatus(TaskStatus status){
        this.status = status;
    }

}