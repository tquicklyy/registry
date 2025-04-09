package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.*;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    public enum Role {
        CITIZEN,
        ADMIN,
        EMPLOYEE;
    }

    @Enumerated(EnumType.STRING) // Указываем, что это Enum, и будем хранить его в строковом формате
    private Role role = Role.CITIZEN;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 100, message = "Логин должен содержать от 4 до 100 символов")
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank
    @Size(min = 8, max = 100, message = "Пароль должен содержать от 8 до 100 символов")
    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled = true;

    @NotBlank
    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$")
    @Size(max = 100, message = "Размер имени не должен превышать 100 символов")
    private String name;

    @NotBlank
    @Column(name = "surname", nullable = false)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$")
    @Size(max = 100, message = "Размер фамилии не должен превышать 100 символов")
    private String surname;

    @Column(name = "patronymic", nullable = true)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$")
    @Size(max = 100, message = "Размер отчества не должен превышать 100 символов")
    private String patronymic;

    @NotNull
    @Past(message = "Введена некорректная дата рождения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Any
    @Column(name = "content_type")
    @AnyDiscriminatorValue(discriminator = "Citizen", entity = Citizen.class)
    @AnyDiscriminatorValue(discriminator = "Employee", entity = Employee.class)
    @AnyKeyJavaType(LongJavaType.class)
    @JoinColumn(name = "person_id")
    private Object person;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
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
}
