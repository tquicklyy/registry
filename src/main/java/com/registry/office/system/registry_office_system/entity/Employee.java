package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Size(max = 255)
    @Column(name = "position", nullable = false)
    private String position;

    @NotNull
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    public Employee() {
    }

    public Employee(String position, LocalDate hireDate) {
        this.position = position;
        this.hireDate = hireDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPosition(@NotNull @Size(max = 255) String position) {
        this.position = position;
    }

    public void setHireDate(@NotNull LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getId() {
        return id;
    }

    public @NotNull @Size(max = 255) String getPosition() {
        return position;
    }

    public @NotNull LocalDate getHireDate() {
        return hireDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
