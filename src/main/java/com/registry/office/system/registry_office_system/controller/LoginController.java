package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String getLoginForm(User user) {
        return "login";
    }
}
