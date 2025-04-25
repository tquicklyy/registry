package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


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
}
