package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "divorce_registrations")
public class DivorceRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "marriage_id", nullable = false)
    private MarriageRegistration marriageRegistration;

    @NotNull
    @Column(name = "divorce_date", nullable = false)
    private LocalDate divorceDate;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

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

    public DivorceRegistration() {
    }

    public DivorceRegistration(LocalDate divorceDate, String reason) {
        this.divorceDate = divorceDate;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public MarriageRegistration getMarriageRegistration() {
        return marriageRegistration;
    }

    public LocalDate getDivorceDate() {
        return divorceDate;
    }

    public String getReason() {
        return reason;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMarriageRegistration(MarriageRegistration marriageRegistration) {
        this.marriageRegistration = marriageRegistration;
    }

    public void setDivorceDate(LocalDate divorceDate) {
        this.divorceDate = divorceDate;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "DivorceRegistration{" +
                "id=" + id +
                ", marriageRegistration=" + marriageRegistration +
                ", divorceDate=" + divorceDate +
                ", reason='" + reason + '\'' +
                '}';
    }
}
