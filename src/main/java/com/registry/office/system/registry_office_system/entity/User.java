package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.*;
import org.hibernate.type.descriptor.java.LongJavaType;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    public enum Role {
        CITIZEN,
        ADMIN,
        EMPLOYEE;
    }

    @Enumerated(EnumType.STRING) // Указываем, что это Enum, и будем хранить его в строковом формате
    private Role role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private boolean enabled;

    @NotNull
    @Column(name = "name", nullable = false)
    @Size(max = 100)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    @Size(max = 100)
    private String surname;

    @Column(name = "patronymic", nullable = true)
    @Size(max = 100)
    private String patronymic;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Any
    @Column(name = "content_type")
    @AnyDiscriminatorValue(discriminator = "Citizen", entity = Citizen.class)
    @AnyDiscriminatorValue(discriminator = "Employee", entity = Employee.class)
    @AnyKeyJavaType(LongJavaType.class)
    @JoinColumn(name = "person_id")
    private Object person;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Object getPerson() {
        return person;
    }

    public void setPerson(Object person) {
        this.person = person;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @NotNull LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(@NotNull LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}