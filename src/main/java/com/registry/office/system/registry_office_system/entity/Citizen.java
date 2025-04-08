package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "citizen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "mother", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BirthRecord> birthRecordsAsMother = new ArrayList<>();

    @OneToMany(mappedBy = "father", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BirthRecord> birthRecordsAsFather = new ArrayList<>();

    private enum Gender {
        M, F;
    }

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @NotNull
    @Column(name = "place_of_birth", nullable = false)
    @Size(max = 255)
    private String placeOfBirth;

    @NotNull
    @Column(name = "nationality", nullable = false)
    @Size(max = 100)
    private String nationality;

    public Citizen() { }

    public int getId() {
        return id;
    }

    @Transient
    public List<BirthRecord> getBirthRecords() {
        List<BirthRecord> allRecords = new ArrayList<>();
        allRecords.addAll(birthRecordsAsMother);
        allRecords.addAll(birthRecordsAsFather);
        return allRecords;
    }

    public List<BirthRecord> getBirthRecordsAsMother() {
        return birthRecordsAsMother;
    }

    public List<BirthRecord> getBirthRecordsAsFather() {
        return birthRecordsAsFather;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public Gender getGender() {
        return gender;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setBirthRecordsAsMother(List<BirthRecord> birthRecordsAsMother) {
        this.birthRecordsAsMother = birthRecordsAsMother;
    }

    public void setBirthRecordsAsFather(List<BirthRecord> birthRecordsAsFather) {
        this.birthRecordsAsFather = birthRecordsAsFather;
    }

}
