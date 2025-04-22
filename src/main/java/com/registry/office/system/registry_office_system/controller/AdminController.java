package com.registry.office.system.registry_office_system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('EMPLOYEE')")
public class AdminController {

    @GetMapping("")
    public String getAdminForm() {
        return "admin";
    }

    @GetMapping("/birth-record")
    public String getBirthRecordForm() {
        return "birth-record";
    }

    @GetMapping("/marriage-registration")
    public String getMarriageRegistrationForm() {
        return "marriage-registration";
    }

    @GetMapping("/death-registration")
    public String getDeathRegistrationForm() {
        return "death-registration";
    }

    @GetMapping("/divorce-registration")
    public String getDivorceRegistration() {
        return "divorce-registration";
    }
}
