package com.registry.office.system.registry_office_system.service.user;

import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.Employee;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.employee.EmployeeRepository;
import com.registry.office.system.registry_office_system.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public User registerUser(User newUser) {

        Object person = newUser.getPerson();

        if(newUser.getRole().name().equals("CITIZEN")) {
            if(person == null) person = new Citizen();
            Citizen citizen = citizenRepository.save((Citizen) person);
            newUser.setPersonId(citizen.getId());
        } else {
            if(person == null) person = new Employee();
            Employee employee = employeeRepository.save((Employee) person);
            newUser.setPersonId(employee.getId());
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return userRepository.save(newUser);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsBySnils(String snils) {
        return userRepository.existsBySnils(snils);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public Optional<User> findBySnils(String snils) {
        return userRepository.findBySnils(snils);
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        Role roleOfUser = user.getRole();

        if(roleOfUser.name().equals("CITIZEN")) {
            user.setPerson(citizenRepository.findById(user.getPersonId()));
        } else {
            user.setPerson(employeeRepository.findById(user.getPersonId()));
        }

        return Optional.of(user);
    }

    public User saveUser(User user) {
        Object person = user.getPerson();

        if(user.getRole().name().equals("CITIZEN")) {
            if(person == null) person = new Citizen();
            Citizen citizen = citizenRepository.save((Citizen) person);
            user.setPersonId(citizen.getId());
        } else {
            if(person == null) person = new Employee();
            Employee employee = employeeRepository.save((Employee) person);
            user.setPersonId(employee.getId());
        }

        return userRepository.save(user);
    }

    public User updateUser(User beforeUser, User newUser) {
        beforeUser.setName(newUser.getName());
        beforeUser.setSurname(newUser.getSurname());
        beforeUser.setPatronymic(newUser.getPatronymic());
        beforeUser.setDateOfBirth(newUser.getDateOfBirth());
        beforeUser.setUsername(newUser.getUsername());
        beforeUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        beforeUser.setRegistered(true);
        beforeUser.setPersonId(newUser.getPersonId());
        beforeUser.setPerson(newUser.getPerson());
        beforeUser.setPhone(newUser.getPhone());
        beforeUser.setGender(newUser.getGender());
        beforeUser.setEmail(newUser.getEmail());

        return beforeUser;
    }

}
