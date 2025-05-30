package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "birth_records")
public class BirthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_id", nullable = false)
    private Citizen child;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "father_id", nullable = false)
    private Citizen father;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mother_id", nullable = false)
    private Citizen mother;

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

    public Citizen getChild() {
        return child;
    }

    public void setChild(Citizen child) {
        this.child = child;
    }

    public Citizen getFather() {
        return father;
    }

    public void setFather(Citizen father) {
        this.father = father;
    }

    public Citizen getMother() {
        return mother;
    }

    public void setMother(Citizen mother) {
        this.mother = mother;
    }
}
