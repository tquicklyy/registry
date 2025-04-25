package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.DivorceRegistration;
import com.registry.office.system.registry_office_system.entity.MarriageRegistration;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.divorceRegistration.DivorceRegistrationRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
    DivorceRegistrationRepository divorceRegistrationRepository;

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
        return "divorce";
    }

    @PostMapping("/divorce-registration")
    public String confirmDivorceRegistration(
            @RequestParam(name = "husbandSnils") String husbandSnils,
            @RequestParam(name = "wifeSnils") String wifeSnils,
            @RequestParam(name = "divorceDate") LocalDate divorceDate,
            @RequestParam(name = "causeOfDivorce") String causeOfDivorce,
            Model model) {
        model.addAttribute("husbandSnils", husbandSnils);
        model.addAttribute("wifeSnils", wifeSnils);
        model.addAttribute("registrationDate", divorceDate);

        boolean husbandSnilsMatches = husbandSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}$");
        boolean wifeSnilsMatches = wifeSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}");
        boolean dateCheck = divorceDate.isBefore(LocalDate.now());

        if (!husbandSnilsMatches) {
            model.addAttribute("husbandSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!wifeSnilsMatches) {
            model.addAttribute("wifeSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!dateCheck) {
            model.addAttribute("registrationDateError", "Некорректная дата");
        }

        if (!husbandSnilsMatches || !wifeSnilsMatches || !dateCheck) {
            return "divorce";
        }

        Optional<User> husbandOptional = userService.findBySnils(husbandSnils);
        Optional<User> wifeOptional = userService.findBySnils(wifeSnils);

        if (husbandOptional.isEmpty() || !husbandOptional.get().isEnabled()) {
            model.addAttribute("husbandSnilsError", "Пользователь не найден");
        }
        if (wifeOptional.isEmpty() || !wifeOptional.get().isEnabled()) {
            model.addAttribute("wifeSnilsError", "Пользователь не найден");
        }

        if (husbandOptional.isEmpty() || !husbandOptional.get().isEnabled() || wifeOptional.isEmpty() || !wifeOptional.get().isEnabled()) {
            return "divorce";
        }

        Citizen husband = citizenRepository.findById(husbandOptional.get().getPersonId()).get();
        Citizen wife = citizenRepository.findById(wifeOptional.get().getPersonId()).get();

        boolean isMarriagePossible = true;
        MarriageRegistration marriageRegistrationHusband = null;
        MarriageRegistration marriageRegistrationWife = null;

        for (MarriageRegistration marriageRegistration: husband.getMarriagesAsHusband()) {
            if (marriageRegistration.getDivorceDate() == null) {
                marriageRegistrationHusband = marriageRegistration;
                break;
            }
        }

        if (marriageRegistrationHusband == null) {
            model.addAttribute("husbandSnilsError", "Пользователь не состоит в браке");
            isMarriagePossible = false;
        }

        for (MarriageRegistration marriageRegistration: wife.getMarriagesAsWife()) {
            if (marriageRegistration.getDivorceDate() == null) {
                marriageRegistrationWife = marriageRegistration;
                break;
            }
        }

        if (marriageRegistrationWife == null) {
            model.addAttribute("wifeSnilsError", "Пользователь не состоит в браке");
            isMarriagePossible = false;
        }

        if (!isMarriagePossible) {
            return "divorce";
        }

        if (!marriageRegistrationHusband.equals(marriageRegistrationWife)) {
            model.addAttribute("divorceErrorMessage", "Пользователи состоят в разных браках");
        }

        DivorceRegistration divorceRegistration = new DivorceRegistration();
        divorceRegistration.setMarriageRegistration(marriageRegistrationHusband);
        divorceRegistration.setDivorceDate(divorceDate);
        divorceRegistration.setReason(causeOfDivorce);
        marriageRegistrationHusband.setDivorceDate(divorceDate);
        divorceRegistrationRepository.save(divorceRegistration);

        model.addAttribute("divorceMessage", "Запись добавлена");

        return "divorce";
    }
}
