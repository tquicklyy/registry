package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.config.CustomUserDetails;
import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.MarriageRegistration;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Gender;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.marriageRegistration.MarriageRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    MarriageRegistrationRepository marriageRegistrationRepository;

    @Autowired
    CitizenRepository citizenRepository;

    @GetMapping("/my-documents")
    public String getMyDocuments(Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();
        String name = user.getSurname() + " " + user.getName();
        if (user.getPatronymic() != null) {
            name += " " + user.getPatronymic();
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateOfBirth = user.getDateOfBirth().format(formatter);

        String email = user.getEmail();
        String phone = user.getPhone();
        String gender = user.getGender().equals(Gender.MALE) ? "Мужской": "Женский";
        String snils = user.getSnils();

        Citizen person = citizenRepository.findById(user.getPersonId()).get();
        List<MarriageRegistration> marriageRegistrations = new ArrayList<>();
        if (user.getGender().equals(Gender.MALE)) {
            marriageRegistrations = person.getMarriagesAsHusband();
        } else {
            marriageRegistrations = person.getMarriagesAsWife();
        }

        ArrayList<String> information = new ArrayList<>();
        for (MarriageRegistration marriageRegistration : marriageRegistrations) {
            if (marriageRegistration.getDivorceDate() == null) {
                information.add(0, "Пользователь состоит в браке.");
            } else {
                information.add(String.format("Пользователь состоял в браке с %s по %s", marriageRegistration.getRegistrationDate().format(formatter), marriageRegistration.getDivorceDate().format(formatter)));
            }
        }

        model.addAttribute("name", name);
        model.addAttribute("date_of_birth", dateOfBirth);
        model.addAttribute("email", email);
        model.addAttribute("phone", phone);
        model.addAttribute("gender", gender);
        model.addAttribute("snils", snils);
        model.addAttribute("items", information);

        return "profile";
    }
}
