package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "registry_records")
public class RegistryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "record_type", nullable = false)
    private RecordType recordType;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @NotNull
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private enum RecordType {
        birth,
        marriage,
        divorce,
        death
    }

    public RegistryRecord() {
    }

    public RegistryRecord(RecordType recordType, LocalDate registrationDate) {
        this.recordType = recordType;
        this.registrationDate = registrationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecordType(@NotNull RecordType recordType) {
        this.recordType = recordType;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public void setRegistrationDate(@NotNull LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setEmployee(@NotNull Employee employee) {
        this.employee = employee;
    }

    public int getId() {
        return id;
    }

    public @NotNull RecordType getRecordType() {
        return recordType;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public @NotNull LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public @NotNull Employee getEmployee() {
        return employee;
    }

    @Override
    public String toString() {
        return "RegistryRecord{" +
                "id=" + id +
                ", recordType=" + recordType +
                ", citizen=" + citizen +
                ", registrationDate=" + registrationDate +
                ", employee=" + employee +
                '}';
    }
}
