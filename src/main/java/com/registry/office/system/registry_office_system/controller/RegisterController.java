package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String getRegisterForm(User user) {
        return "register";
    }

    @PostMapping("/register")
    public String confirmRegister(@RequestParam(name = "isEmployee", defaultValue = "false") boolean isEmployee,
                                  @Valid @ModelAttribute("user") User user,
                                  BindingResult result) {
        if(isEmployee) {
            user.setEnabled(false);
            user.setRole(Role.EMPLOYEE);
        }

        if(Objects.equals(user.getEmail(), "")) {
            user.setEmail(null);
        }

        if(Objects.equals(user.getPatronymic(), "")) {
            user.setPatronymic(null);
        }

        if(userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "user.username.exists", "Этот логин уже занят");
        }

        if(userService.existsBySnils(user.getSnils())) {
            result.rejectValue("snils", "user.snils.exists", "Пользователь с данным СНИЛС'ом уже существует");
        }

        if(user.getEmail() != null && userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "user.email.exists", "Этот адрес электронной почты уже занят");
        }

        if(userService.existsByPhone(user.getPhone())) {
            result.rejectValue("phone", "user.phone.exists", "Этот номер телефона уже занят");
        }

        if(result.hasErrors()) return "register";

        if(userService.existsBySnils(user.getSnils()) && !userService.findBySnils(user.getSnils()).get().isRegistered()) {
            User registeredUser = userService.updateUser(userService.findBySnils(user.getSnils()).get(), user);
            userService.saveUser(registeredUser);
        } else {
            user.setRegistered(true);
            userService.registerUser(user);
        }

        return "redirect:/login";
    }
}
