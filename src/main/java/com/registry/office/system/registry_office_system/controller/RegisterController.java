package com.registry.office.system.registry_office_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String getRegisterForm() {
        return "register";
    }
}
