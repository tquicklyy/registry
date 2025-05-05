package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "marriage_registration")
public class MarriageRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "husband_id", nullable = false)
    private Citizen husband;

    @ManyToOne
    @JoinColumn(name = "wife_id", nullable = false)
    private Citizen wife;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate = LocalDate.now();

    @Column(name = "divorce_date")
    private LocalDate divorceDate;

    @NotNull
    @Column(name = "region_code", nullable = false)
    @Pattern(regexp = "^[IVXLCDM]+$", message = "Неверный формат кода региона")
    private String regionCode;

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    @NotNull
    @Column(name = "registry_code", nullable = false)
    @Pattern(regexp = "^[А-Я]{2}$", message = "Неверный формат регистрационного кода")
    @Size(min = 2, max = 2)
    private String registryCode;

    public String getRegistryCode() {
        return registryCode;
    }

    public void setRegistryCode(String registryCode) {
        this.registryCode = registryCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Citizen getHusband() {
        return husband;
    }

    public void setHusband(Citizen husband) {
        this.husband = husband;
    }

    public Citizen getWife() {
        return wife;
    }

    public void setWife(Citizen wife) {
        this.wife = wife;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public LocalDate getDivorceDate() {
        return divorceDate;
    }

    public void setDivorceDate(LocalDate divorceDate) {
        this.divorceDate = divorceDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MarriageRegistration that = (MarriageRegistration) o;
        return id == that.id && Objects.equals(husband, that.husband) && Objects.equals(wife, that.wife) && Objects.equals(registrationDate, that.registrationDate) && Objects.equals(divorceDate, that.divorceDate);
    }
}