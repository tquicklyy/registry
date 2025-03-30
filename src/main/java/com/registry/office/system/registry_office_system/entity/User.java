package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.*;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
