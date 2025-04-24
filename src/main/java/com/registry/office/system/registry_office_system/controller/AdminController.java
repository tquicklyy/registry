package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.DeathRegistration;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.deathRegistration.DeathRegistrationRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('EMPLOYEE')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    DeathRegistrationRepository deathRegistrationRepository;

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
    public String getDeathRegistrationForm(
            DeathRegistration deathRegistration
    ) {
        return "death-registration";
    }

    @PostMapping("/death-registration")
    public String confirmDeathRegistration(
            @RequestParam(name = "personSnils") String personSnils,
            @Valid @ModelAttribute("deathRegistration") DeathRegistration deathRegistration,
            BindingResult result,
            Model model
            ) {

        model.addAttribute("personSnils", personSnils);
        boolean snilsMatches = personSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}$");

        if(!snilsMatches) {
            model.addAttribute("personSnilsError", "Неверный формат СНИЛС'а");
        }

        if(result.hasErrors() || !snilsMatches) return "death-registration";

        Optional<User> userOptional = userService.findBySnils(personSnils);

        if(userOptional.isEmpty()) {
            model.addAttribute("confirmDeathError", "Пользователь не найден");
            return "death-registration";
        }

        User user = userOptional.get();

        if(user.getDateOfBirth().isAfter(deathRegistration.getDeathDate())) {
            model.addAttribute("confirmDeathError", "Некорректная дата");
            return "death-registration";
        }

        Citizen citizen = citizenRepository.findById(user.getPersonId()).get();
        if(citizen.getDeathRegistration() != null) {
            model.addAttribute("confirmDeathError", "Запись уже существует");
            return "death-registration";
        }

        deathRegistration.setCitizen(citizen);
        user.setEnabled(false);
        deathRegistrationRepository.save(deathRegistration);

        model.addAttribute("deathRegistration", new DeathRegistration());
        model.addAttribute("personSnils", "");
        model.addAttribute("confirmDeathError", "Запись добавлена");
        return "death-registration";

    }

    @GetMapping("/divorce-registration")
    public String getDivorceRegistration() {
        return "divorce-registration";
    }
}
