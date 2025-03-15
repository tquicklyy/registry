package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "passports")
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Invalid series")
    @Column(name = "series", nullable = false)
    @Size(min = 4, max = 4)
    private String series;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Invalid series")
    @Size(min = 6, max = 6)
    @Column(name = "number", nullable = false)
    private String number;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @NotNull
    @Column(name = "issuer", nullable = false)
    @Size(max = 100)
    private String issuer;

    public int getId() {
        return id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public String getSeries() {
        return series;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

}
