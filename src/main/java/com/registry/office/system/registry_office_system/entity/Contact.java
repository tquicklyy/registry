package com.registry.office.system.registry_office_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "contacts")
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "citizen_id", nullable = false)
    private Citizen citizen;

    @NotNull
    @Size(max = 20)
    @Pattern(regexp = "^\\+?([78])[-\\s]?[0-9]{3}[-\\s]?[0-9]{3}[-\\s]?[0-9]{2}[-\\s]?[0-9]{2}$",
            message = "Not correct phone number")
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Size(max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    @Column(name = "email", nullable = false)
    private String email;

    public Contact() {
    }

    public Contact(String phone, String email) {
        this.phone = phone;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", citizen=" + citizen +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
