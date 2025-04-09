package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String getLoginForm(@RequestParam(value = "error", required = false) String error,
                               User user, Model model) {
        if(error != null) {
            model.addAttribute("loginError", true);
            model.addAttribute("errorMessage",
                    "Не удалось войти в аккаунт. Проверьте введённые данные");
        }

        return "login";
    }
}
