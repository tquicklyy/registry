package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate = LocalDate.now();

    public Employee() {
    }

    public Employee(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public @NotNull LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", hireDate=" + hireDate +
                '}';
    }
}
