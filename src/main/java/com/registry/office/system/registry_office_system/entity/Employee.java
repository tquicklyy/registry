package com.registry.office.system.registry_office_system.entity;

import jakarta.annotation.Nullable;
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
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "surname", nullable = false)
    private String surname;

    @Size(max = 255)
    @Column(name = "patronymic")
    private String patronymic;

    @NotNull
    @Size(max = 255)
    @Column(name = "position", nullable = false)
    private String position;

    @NotNull
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    public Employee() {
    }

    public Employee(String name, String surname, String patronymic, String position, LocalDate hireDate) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.position = position;
        this.hireDate = hireDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public void setSurname(@NotNull @Size(max = 255) String surname) {
        this.surname = surname;
    }

    public void setPatronymic(@NotNull @Size(max = 255) String patronymic) {
        this.patronymic = patronymic;
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

    public @NotNull String getName() {
        return name;
    }

    public @NotNull @Size(max = 255) String getSurname() {
        return surname;
    }

    public @NotNull @Size(max = 255) String getPatronymic() {
        return patronymic;
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
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", position='" + position + '\'' +
                ", hireDate=" + hireDate +
                '}';
    }
}
