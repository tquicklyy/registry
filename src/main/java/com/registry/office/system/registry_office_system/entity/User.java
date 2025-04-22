package com.registry.office.system.registry_office_system.entity;

import com.registry.office.system.registry_office_system.enums.Gender;
import com.registry.office.system.registry_office_system.enums.Role;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Check;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Enumerated(EnumType.STRING) // Указываем, что это Enum, и будем хранить его в строковом формате
    @Column(name = "role", nullable = false)
    private Role role = Role.CITIZEN;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Check(constraints = "gender IN ('MALE', 'FEMALE')")
    @Column(name = "gender", nullable = false)
    private Gender gender;

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

    @Column(name = "is_registered")
    private boolean isRegistered;

    @NotBlank
    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Неверный формат имени")
    @Size(max = 100, message = "Размер имени не должен превышать 100 символов")
    private String name;

    @NotBlank
    @Column(name = "surname", nullable = false)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]+$", message = "Неверный формат фамилии")
    @Size(max = 100, message = "Размер фамилии не должен превышать 100 символов")
    private String surname;

    @Column(name = "patronymic", nullable = true)
    @Pattern(regexp = "^[А-Яа-яA-Za-z\\-\\s]*$", message = "Неверный формат отчества")
    @Size(max = 100, message = "Размер отчества не должен превышать 100 символов")
    private String patronymic;

    @NotBlank
    @Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3} \\d{2}$", message = "Неверный формат СНИЛС'а")
    @Column(name = "snils", unique = true)
    private String snils;

    @NotNull
    @Past(message = "Введена некорректная дата рождения")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Size(max = 20, message = "Номер телефона не должен превышать 20 символов")
    @Pattern(regexp = "^\\+?([78])[-\\s]?[0-9]{3}[-\\s]?[0-9]{3}[-\\s]?[0-9]{2}[-\\s]?[0-9]{2}$",
            message = "Неверный формат номера телефона")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Size(max = 255, message = "Номер телефона не должен превышать 255 символов")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Неверный формат электронной почты")
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "person_id")
    private int personId;

    @Transient
    private Object person;

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

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

    public @NotNull Gender getGender() {
        return gender;
    }

    public void setGender(@NotNull Gender gender) {
        this.gender = gender;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}