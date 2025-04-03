package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.User;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @GetMapping("/register")
    public String getRegisterForm(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String confirmRegister(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if(result.hasErrors()) return "register";
        return "login";
    }
}
