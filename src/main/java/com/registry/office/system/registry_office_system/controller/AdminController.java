package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.entity.*;
import com.registry.office.system.registry_office_system.enums.Gender;
import com.registry.office.system.registry_office_system.repository.birthRecord.BirthRecordRepository;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.divorceRegistration.DivorceRegistrationRepository;
import com.registry.office.system.registry_office_system.repository.marriageRegistration.MarriageRegistrationRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import com.registry.office.system.registry_office_system.repository.deathRegistration.DeathRegistrationRepository;
import jakarta.validation.Valid;
import org.hibernate.usertype.UserCollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

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

    @Autowired
    DivorceRegistrationRepository divorceRegistrationRepository;

    @Autowired
    MarriageRegistrationRepository marriageRegistrationRepository;

    @Autowired
    BirthRecordRepository birthRecordRepository;

    @GetMapping("")
    public String getAdminForm() {
        return "admin";
    }

    @GetMapping("/birth-record")
    public String getBirthRecordForm() {
        return "birth";
    }

    @PostMapping("/birth-record")
    public String confirmBirthRecord(
            @RequestParam(name = "husbandSnils") String husbandSnils,
            @RequestParam(name = "wifeSnils") String wifeSnils,
            @RequestParam(name = "childSnils") String childSnils,
            @RequestParam(name = "birthDate") LocalDate birthDate,
            @RequestParam(name = "regionCode") String regionCode,
            @RequestParam(name = "registryCode") String registryCode,
            Model model) {
        model.addAttribute("husbandSnils", husbandSnils);
        model.addAttribute("wifeSnils", wifeSnils);
        model.addAttribute("childSnils", childSnils);
        model.addAttribute("birthDate", birthDate);

        boolean husbandSnilsMatches = husbandSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}$");
        boolean wifeSnilsMatches = wifeSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}");
        boolean childSnilsMatches = childSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}");
        boolean dateCheck = birthDate.isBefore(LocalDate.now());
        boolean regionCodeMatches = regionCode.matches("^[IVXLCDM]+$");
        boolean registryCodeMatches = registryCode.matches("^[А-Я]{2}$");

        if (!husbandSnilsMatches) {
            model.addAttribute("husbandSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!wifeSnilsMatches) {
            model.addAttribute("wifeSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!childSnilsMatches) {
            model.addAttribute("childSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!dateCheck) {
            model.addAttribute("registrationDateError", "Некорректная дата");
        }

        if (!regionCodeMatches) {
            model.addAttribute("regionCodeError", "Неверный формат");
        }
        if (!registryCodeMatches) {
            model.addAttribute("registryCodeError", "Неверный формат");
        }

        if (!husbandSnilsMatches || !wifeSnilsMatches || !childSnilsMatches || !dateCheck || !registryCodeMatches || !regionCodeMatches) {
            return "birth";
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
            return "birth";
        }

        Optional<User> childOptional = userService.findBySnils(childSnils);
        if (!childOptional.isEmpty()) {
            model.addAttribute("childSnilsError", "Пользователь уже существует");
            return "birth";
        }

        User child = new User();
        String uuid = UUID.randomUUID().toString();
        child.setGender(Gender.MALE);
        child.setUsername(uuid);
        child.setPassword(uuid);
        child.setEnabled(true);
        child.setRegistered(false);
        child.setName("Отсутсутсвует");
        child.setSurname("Отсутсутсвует");
        child.setPatronymic("Отсутсутсвует");
        child.setSnils(childSnils);
        child.setDateOfBirth(birthDate);

        Citizen childCitizen = new Citizen();
        childCitizen = citizenRepository.save(childCitizen);
        child.setPersonId(childCitizen.getId());
        child.setPerson(childCitizen);

        BirthRecord birthRecord = new BirthRecord();
        Citizen citizenFather = citizenRepository.findById(husbandOptional.get().getPersonId()).get();
        Citizen citizenMother = citizenRepository.findById(wifeOptional.get().getPersonId()).get();
        birthRecord.setFather(citizenFather);
        birthRecord.setMother(citizenMother);
        birthRecord.setChild(childCitizen);
        birthRecord.setRegistryCode(registryCode);
        birthRecord.setRegionCode(regionCode);
        birthRecordRepository.save(birthRecord);
        userService.registerUser(child);

        model.addAttribute("birthMessage", "Запись добавлена");
        return "birth";
    }

    @GetMapping("/marriage-registration")
    public String getMarriageRegistrationForm() {
        return "marriage";
    }

    @PostMapping("/marriage-registration")
    public String confirmMarriageRegistration(
            @RequestParam(name = "husbandSnils") String husbandSnils,
            @RequestParam(name = "wifeSnils") String wifeSnils,
            @RequestParam(name = "registrationDate") LocalDate registrationDate,
            @RequestParam(name = "regionCode") String regionCode,
            @RequestParam(name = "registryCode") String registryCode,
            Model model) {
        model.addAttribute("husbandSnils", husbandSnils);
        model.addAttribute("wifeSnils", wifeSnils);
        model.addAttribute("registrationDate", registrationDate);

        boolean husbandSnilsMatches = husbandSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}$");
        boolean wifeSnilsMatches = wifeSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}");
        boolean dateCheck = registrationDate.isBefore(LocalDate.now());
        boolean regionCodeMatches = regionCode.matches("^[IVXLCDM]+$");
        boolean registryCodeMatches = registryCode.matches("^[А-Я]{2}$");

        if (!husbandSnilsMatches) {
            model.addAttribute("husbandSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!wifeSnilsMatches) {
            model.addAttribute("wifeSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!dateCheck) {
            model.addAttribute("registrationDateError", "Некорректная дата");
        }

        if (!regionCodeMatches) {
            model.addAttribute("regionCodeError", "Неверный формат");
        }
        if (!registryCodeMatches) {
            model.addAttribute("registryCodeError", "Неверный формат");
        }

        if (!husbandSnilsMatches || !wifeSnilsMatches || !dateCheck || !registryCodeMatches || !regionCodeMatches) {
            return "marriage";
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
            return "marriage";
        }

        if (husbandOptional.get().getGender() == wifeOptional.get().getGender()) {
            model.addAttribute("confirmMarriageErrorMessage", "Нельзя зарегистрировать брак людей одного пола");
            return "marriage";
        }

        Citizen husband = citizenRepository.findById(husbandOptional.get().getPersonId()).get();
        Citizen wife = citizenRepository.findById(wifeOptional.get().getPersonId()).get();

        boolean isMarriagePossible = true;

        for (MarriageRegistration marriageRegistration: husband.getMarriagesAsHusband()) {
            if (marriageRegistration.getDivorceDate() == null) {
                model.addAttribute("husbandSnilsError", "Пользователь состоит в браке");
                isMarriagePossible = false;
                break;
            }
        }

        for (MarriageRegistration marriageRegistration: wife.getMarriagesAsWife()) {
            if (marriageRegistration.getDivorceDate() == null) {
                model.addAttribute("wifeSnilsError", "Пользователь состоит в браке");
                isMarriagePossible = false;
                break;
            }
        }

        if (!isMarriagePossible) {
            return "marriage";
        }

        MarriageRegistration marriageRegistration = new MarriageRegistration();
        marriageRegistration.setHusband(husband);
        marriageRegistration.setWife(wife);
        marriageRegistration.setRegistrationDate(registrationDate);
        marriageRegistration.setRegionCode(regionCode);
        marriageRegistration.setRegistryCode(registryCode);
        marriageRegistrationRepository.save(marriageRegistration);

        model.addAttribute("confirmMarriageMessage", "Запись добавлена!");

        return "marriage";
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
        return "divorce";
    }

    @PostMapping("/divorce-registration")
    public String confirmDivorceRegistration(
            @RequestParam(name = "husbandSnils") String husbandSnils,
            @RequestParam(name = "wifeSnils") String wifeSnils,
            @RequestParam(name = "divorceDate") LocalDate divorceDate,
            @RequestParam(name = "causeOfDivorce") String causeOfDivorce,
            @RequestParam(name = "regionCode") String regionCode,
            @RequestParam(name = "registryCode") String registryCode,
            Model model) {
        model.addAttribute("husbandSnils", husbandSnils);
        model.addAttribute("wifeSnils", wifeSnils);
        model.addAttribute("registrationDate", divorceDate);

        boolean husbandSnilsMatches = husbandSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}$");
        boolean wifeSnilsMatches = wifeSnils.matches("^\\d{3}-\\d{3}-\\d{3} \\d{2}");
        boolean dateCheck = divorceDate.isBefore(LocalDate.now());
        boolean regionCodeMatches = regionCode.matches("^[IVXLCDM]+$");
        boolean registryCodeMatches = registryCode.matches("^[А-Я]{2}$");

        if (!husbandSnilsMatches) {
            model.addAttribute("husbandSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!wifeSnilsMatches) {
            model.addAttribute("wifeSnilsError", "Неверный формат СНИЛС'а");
        }
        if (!dateCheck) {
            model.addAttribute("registrationDateError", "Некорректная дата");
        }

        if (!regionCodeMatches) {
            model.addAttribute("regionCodeError", "Неверный формат");
        }
        if (!registryCodeMatches) {
            model.addAttribute("registryCodeError", "Неверный формат");
        }

        if (!husbandSnilsMatches || !wifeSnilsMatches || !dateCheck || !registryCodeMatches || !regionCodeMatches) {
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
        divorceRegistration.setRegionCode(regionCode);
        divorceRegistration.setRegistryCode(registryCode);
        marriageRegistrationHusband.setDivorceDate(divorceDate);
        divorceRegistrationRepository.save(divorceRegistration);

        model.addAttribute("divorceMessage", "Запись добавлена");

        return "divorce";
    }
}
