package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String confirmRegister(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if(result.hasErrors()) return "register";
        userService.registerUser(user);
        return "redirect:/login";
    }
}
