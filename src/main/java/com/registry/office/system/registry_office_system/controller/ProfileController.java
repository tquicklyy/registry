package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.config.CustomUserDetails;
import com.registry.office.system.registry_office_system.entity.*;
import com.registry.office.system.registry_office_system.enums.Gender;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.divorceRegistration.DivorceRegistrationRepository;
import com.registry.office.system.registry_office_system.repository.marriageRegistration.MarriageRegistrationRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
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

    @Autowired
    UserService userService;

    @Autowired
    DivorceRegistrationRepository divorceRegistrationRepository;

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

        List<BirthRecord> birthRecords;
        if (user.getGender().equals(Gender.MALE)) {
            birthRecords = person.getBirthRecordsAsFather();
        } else {
            birthRecords = person.getBirthRecordsAsMother();
        }

        Citizen additionalCitizen;
        User additionalUser;
        DivorceRegistration divorceRegistration;
        ArrayList<String> blockOfInfo;
        ArrayList<ArrayList<String>> information = new ArrayList<>();

        for (MarriageRegistration marriageRegistration : marriageRegistrations) {
            blockOfInfo = new ArrayList<>();
            if (user.getGender().equals(Gender.MALE)) {
                additionalCitizen = marriageRegistration.getWife();
            } else {
                additionalCitizen = marriageRegistration.getHusband();
            }

            additionalUser = userService.findByRoleAndPersonId(Role.CITIZEN, additionalCitizen.getId()).get();

            if (marriageRegistration.getDivorceDate() == null) {
                blockOfInfo.add("Пользователь состоит в браке.");
                blockOfInfo.add(String.format("ФИО партнера: %s %s %s", additionalUser.getSurname(), additionalUser.getName(), additionalUser.getPatronymic()));
                blockOfInfo.add(String.format("СНИЛС партнера: %s", additionalUser.getSnils()));
                blockOfInfo.add(String.format("Дата регистрации брака: %s", marriageRegistration.getRegistrationDate().format(formatter)));
                blockOfInfo.add(String.format("Номер документа: %s-%s № %06d", marriageRegistration.getRegionCode(), marriageRegistration.getRegistryCode(), marriageRegistration.getId()));
                information.add(0, blockOfInfo);
            } else {
                divorceRegistration = divorceRegistrationRepository.findByMarriageRegistration(marriageRegistration).get();
                blockOfInfo.add("Пользователь состоял в браке.");
                blockOfInfo.add(String.format("ФИО партнера: %s %s %s", additionalUser.getSurname(), additionalUser.getName(), additionalUser.getPatronymic()));
                blockOfInfo.add(String.format("СНИЛС партнера: %s", additionalUser.getSnils()));
                blockOfInfo.add(String.format("Дата регистрации брака: %s", marriageRegistration.getRegistrationDate().format(formatter)));
                blockOfInfo.add(String.format("Дата расторжения брака: %s", divorceRegistration.getDivorceDate().format(formatter)));
                blockOfInfo.add(String.format("Номер документа: %s-%s № %06d", divorceRegistration.getRegionCode(), divorceRegistration.getRegistryCode(), divorceRegistration.getId()));
                information.add(blockOfInfo);
            }
        }

        for (BirthRecord birthRecord : birthRecords) {
            blockOfInfo = new ArrayList<>();
            additionalCitizen = birthRecord.getChild();
            additionalUser = userService.findByPersonId(additionalCitizen.getId()).get();
            blockOfInfo.add("У пользователя есть ребенок");
            blockOfInfo.add(String.format("ФИО: %s %s %s", additionalUser.getSurname(), additionalUser.getName(), additionalUser.getPatronymic()));
            blockOfInfo.add(String.format("СНИЛС: %s", additionalUser.getSnils()));
            blockOfInfo.add(String.format("Номер документа: %s-%s № %06d", birthRecord.getRegionCode(), birthRecord.getRegistryCode(), birthRecord.getId()));
            information.add(blockOfInfo);
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
