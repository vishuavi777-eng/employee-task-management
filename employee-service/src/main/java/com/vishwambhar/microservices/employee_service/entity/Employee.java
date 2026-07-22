package com.vishwambhar.microservices.employee_service.entity;

import com.vishwambhar.microservices.employee_service.enums.Department;
import com.vishwambhar.microservices.employee_service.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_employee_code", columnNames = "employee_code"),
                @UniqueConstraint(name = "uk_employee_email", columnNames = "email")
        },
        indexes = {
                @Index(name = "idx_employee_status", columnList = "status"),
                @Index(
                        name = "idx_employee_department",
                        columnList = "department"
                )
        }
)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "employee_code",
            nullable = false,
            length = 30
    )
    private String employeeCode;

    @Column(
            name = "first_name",
            nullable = false,
            length = 100
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            length = 100
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            length = 150
    )
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "department",
            nullable = false,
            length = 50
    )
    private Department department;

    @Column(
            name = "designation",
            nullable = false,
            length = 100
    )
    private String designation;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    private EmployeeStatus status;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        LocalDateTime now = LocalDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = EmployeeStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDetails(
            String firstName,
            String lastName,
            String email,
            Department department,
            String designation
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.designation = designation;
    }

    public void changeStatus(EmployeeStatus status) {
        this.status = status;
    }
}
