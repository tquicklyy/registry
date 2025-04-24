package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "citizens")
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(mappedBy = "citizen")
    private DeathRegistration deathRegistration;

    @OneToMany(mappedBy = "mother", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BirthRecord> birthRecordsAsMother = new ArrayList<>();

    @OneToMany(mappedBy = "father", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BirthRecord> birthRecordsAsFather = new ArrayList<>();

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

    public void setId(int id) {
        this.id = id;
    }

    public void setBirthRecordsAsMother(List<BirthRecord> birthRecordsAsMother) {
        this.birthRecordsAsMother = birthRecordsAsMother;
    }

    public void setBirthRecordsAsFather(List<BirthRecord> birthRecordsAsFather) {
        this.birthRecordsAsFather = birthRecordsAsFather;
    }

    public DeathRegistration getDeathRegistration() {
        return deathRegistration;
    }

    public void setDeathRegistration(DeathRegistration deathRegistration) {
        this.deathRegistration = deathRegistration;
    }


}
