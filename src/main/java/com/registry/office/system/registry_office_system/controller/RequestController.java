package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.config.CustomUserDetails;
import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.Request;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.enums.Status;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.request.RequestRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    UserService userService;

    @GetMapping("")
    public String getRequestsForm(
            @RequestParam(required = false) Status status,
            Model model
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        List<Request> requests;
        ArrayList<ArrayList<String>> infoForForm = new ArrayList<>();
        ArrayList<Status> statuses = new ArrayList<>();
        int index = 0;
        ArrayList<String> currentList;


        if(user.getRole().equals(Role.CITIZEN)) {
            Citizen citizen = citizenRepository.findById(user.getPersonId()).get();
            if(status == Status.ALL) {
                requests = requestRepository.findByApplicant(citizen);
            } else {
                requests = requestRepository.findByStatusAndApplicant(status, citizen);
            }

            for (Request currentRequest: requests) {
                currentList = new ArrayList<>();

                currentList.add(String.valueOf(currentRequest.getId()));
                currentList.add(String.format("%s %s %s", user.getSurname(), user.getName(), user.getPatronymic()));
                currentList.add(currentRequest.getOperation().getDescription());
                currentList.add(currentRequest.getStatus().getDescription());

                infoForForm.add(currentList);
            }

        } else {
            if(status == Status.ALL) {
                requests = requestRepository.findAll();
            } else {
                requests = requestRepository.findByStatus(status);
            }

            User currentUser;

            for (Request currentRequest: requests) {
                currentList = new ArrayList<>();
                currentUser = userService.findByRoleAndPersonId(Role.CITIZEN, currentRequest.getApplicant().getId());

                currentList.add(String.valueOf(currentRequest.getId()));
                currentList.add(String.format("%s %s %s", currentUser.getSurname(), currentUser.getName(), currentUser.getPatronymic()));
                currentList.add(currentRequest.getOperation().getDescription());
                currentList.add(currentRequest.getStatus().getDescription());

                infoForForm.add(currentList);
            }

        }

        statuses.addAll(
                List.of(Status.WAIT,
                        Status.ACCEPT,
                        Status.CANCEL,
                        Status.IN_WORK)
        );

        model.addAttribute("requests", infoForForm);
        model.addAttribute("statuses", statuses);
        if(status != null) model.addAttribute("selectedStatus", status);
        return "requests";
    }

    @GetMapping("/request/{id}")
    public String getSelectedRequest(
            @PathVariable int id,
            Model model
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Request currentRequest = requestRepository.findById(id).get();
        Status status = requestRepository.findById(id).get().getStatus();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        if(user.getRole().equals(Role.EMPLOYEE)) {

            user = userService.findByRoleAndPersonId(Role.CITIZEN, currentRequest.getApplicant().getId());

            if(status.equals(Status.WAIT)) {
                {
                    LocalDateTime currentTime = LocalDateTime.now();

                    currentTime = currentTime.withMinute(0).withSecond(0).plusHours(2);

                    ArrayList<String> visitDates = new ArrayList<>();
                    int countOfVisitDates = 0;

                    while(countOfVisitDates < 31) {

                        if(currentTime.getHour() > 16) {
                            currentTime = currentTime.withHour(8).plusDays(1);
                        }

                        if(currentTime.getHour() < 8) {
                            currentTime = currentTime.withHour(8);
                        }

                        if(!requestRepository.findByVisitDate(currentTime).isEmpty()) {
                            currentTime = currentTime.plusHours(2);
                            continue;
                        }

                        visitDates.add(currentTime.format(formatter));
                        currentTime = currentTime.plusHours(2);
                        countOfVisitDates++;
                    }

                    model.addAttribute("visitDates", visitDates);
                }
            }
        }

        String[] info = new String[5];

        info[0] = String.valueOf(id);
        info[1] = String.format("%s %s %s", user.getSurname(), user.getName(), user.getPatronymic());
        info[2] = currentRequest.getOperation().getDescription();
        if(currentRequest.getVisitDate() != null) {
            info[3] = currentRequest.getVisitDate().format(formatter);
        }
        if(currentRequest.getEmployee() != null) {
            User employee = userService.findByRoleAndPersonId(Role.EMPLOYEE, currentRequest.getEmployee().getId());
            info[4] = String.format("%s %s %s", employee.getSurname(), employee.getName(), employee.getPatronymic());
        }

        model.addAttribute("info", info);

        ArrayList<String> statuses = new ArrayList<>();

        if(status.equals(Status.CANCEL) || status.equals(Status.ACCEPT)) {
            statuses.add(String.valueOf(status));
        } else {
            statuses.add(String.valueOf(status));
            statuses.add(String.valueOf(Status.CANCEL));
        }

        model.addAttribute("statuses" , statuses);

        return "request";
    }
}
