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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Request currentRequest;

        if(user.getRole().equals(Role.CITIZEN)) {
            Citizen citizen = citizenRepository.findById(user.getPersonId()).get();
            if(status == null) {
                requests = requestRepository.findByApplicant(citizen);
            } else {
                requests = requestRepository.findByStatusAndApplicant(status, citizen);
            }

            for(ArrayList<String> list: infoForForm) {
                currentRequest = requests.get(index);
                list.add(String.valueOf(currentRequest.getId()));
                list.add(String.format("%s %s %s", user.getSurname(), user.getName(), user.getPatronymic()));
                list.add(currentRequest.getOperation().getDescription());
                list.add(currentRequest.getStatus().getDescription());
                index++;
            }

        } else {
            if(status == null) {
                requests = requestRepository.findAll();
            } else {
                requests = requestRepository.findByStatus(status);
            }

            User currentUser;

            for(ArrayList<String> list: infoForForm) {
                currentRequest = requests.get(index);
                currentUser = userService.findByRoleAndPersonId(Role.CITIZEN, currentRequest.getApplicant().getId());
                list.add(String.valueOf(currentRequest.getId()));
                list.add(String.format("%s %s %s", currentUser.getSurname(), currentUser.getName(), currentUser.getPatronymic()));
                list.add(currentRequest.getOperation().getDescription());
                list.add(currentRequest.getStatus().getDescription());
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

}
