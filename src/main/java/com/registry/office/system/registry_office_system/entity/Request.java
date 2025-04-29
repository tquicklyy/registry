package com.registry.office.system.registry_office_system.entity;

import com.registry.office.system.registry_office_system.enums.Operation;
import com.registry.office.system.registry_office_system.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "applicant_id")
    private Citizen applicant;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation", nullable = false)
    private Operation operation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Request() {
    }

    public Request(Citizen applicant, Employee employee, LocalDateTime visitDate, Operation operation, Status status) {
        this.applicant = applicant;
        this.employee = employee;
        this.visitDate = visitDate;
        this.operation = operation;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public @NotNull Citizen getApplicant() {
        return applicant;
    }

    public void setApplicant(@NotNull Citizen applicant) {
        this.applicant = applicant;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDateTime getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
