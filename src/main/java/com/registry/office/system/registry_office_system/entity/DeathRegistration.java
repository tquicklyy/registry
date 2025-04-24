package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "death_registrations")
public class DeathRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @NotNull
    @Past(message = "Введена некорректная дата смерти")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "death_date", nullable = false)
    private LocalDate deathDate;

    @NotNull
    @Column(name = "cause_of_death", nullable = false)
    private String causeOfDeath;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate = LocalDate.now();

    public DeathRegistration() {
    }

    public DeathRegistration(LocalDate deathDate, String causeOfDeath, LocalDate registrationDate) {
        this.deathDate = deathDate;
        this.causeOfDeath = causeOfDeath;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public LocalDate getDeathDate() {
        return deathDate;
    }

    public String getCauseOfDeath() {
        return causeOfDeath;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public void setDeathDate(LocalDate deathDate) {
        this.deathDate = deathDate;
    }

    public void setCauseOfDeath(String causeOfDeath) {
        this.causeOfDeath = causeOfDeath;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "DeathRegistration{" +
                "id=" + id +
                ", citizen=" + citizen +
                ", deathDate=" + deathDate +
                ", causeOfDeath='" + causeOfDeath + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
