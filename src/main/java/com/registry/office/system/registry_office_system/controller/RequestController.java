package com.registry.office.system.registry_office_system.controller;

import com.registry.office.system.registry_office_system.config.CustomUserDetails;
import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.Employee;
import com.registry.office.system.registry_office_system.entity.Request;
import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Operation;
import com.registry.office.system.registry_office_system.enums.Role;
import com.registry.office.system.registry_office_system.enums.Status;
import com.registry.office.system.registry_office_system.repository.citizen.CitizenRepository;
import com.registry.office.system.registry_office_system.repository.employee.EmployeeRepository;
import com.registry.office.system.registry_office_system.repository.request.RequestRepository;
import com.registry.office.system.registry_office_system.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/requests")
public class RequestController {

    @Autowired
    RequestRepository requestRepository;

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    EmployeeRepository employeeRepository;

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

    @GetMapping("/request")
    public String updateSelectedRequest(
            @RequestParam("next-status") Status nextStatus,
            @RequestParam int requestId,
            @RequestParam(required = false) String date
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Request currentRequest = requestRepository.findById(requestId).get();

        if(user.getRole().equals(Role.EMPLOYEE)) {
            Employee employee = employeeRepository.findById(user.getPersonId()).get();

            if(nextStatus.equals(Status.IN_WORK)) {
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                 LocalDateTime visitDate = LocalDateTime.parse(date, formatter);

                 currentRequest.setStatus(Status.IN_WORK);
                 currentRequest.setEmployee(employee);
                 currentRequest.setVisitDate(visitDate);
            } else if(nextStatus.equals(Status.ACCEPT)) {
                currentRequest.setStatus(Status.ACCEPT);
            } else {
                currentRequest.setStatus(Status.CANCEL);
            }
        } else {
            currentRequest.setStatus(Status.CANCEL);
        }

        requestRepository.save(currentRequest);
        return String.format("redirect:/requests/request/%s", requestId);
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

                    currentTime = currentTime.withMinute(0).withSecond(0).withNano(0).plusHours(2);

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

    @GetMapping("/new-request")
    public String getNewRequestForm(Model model) {
        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(Operation.BIRTH);
        operations.add(Operation.MARRIAGE);
        operations.add(Operation.DIVORCE);
        operations.add(Operation.DEATH);

        model.addAttribute("operations", operations);

        return "new-request";
    }

    @PostMapping("/new-request")
    public String addNewRequest(
            @RequestParam(name = "operation-select") Operation selectOperation,
            Model model
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Optional<Request> foundOptionalRequest = requestRepository.findByOperation(selectOperation);

        ArrayList<Operation> operations = new ArrayList<>();
        operations.add(Operation.BIRTH);
        operations.add(Operation.MARRIAGE);
        operations.add(Operation.DIVORCE);
        operations.add(Operation.DEATH);

        model.addAttribute("operations", operations);

        if(foundOptionalRequest.isPresent()) {
            Request foundRequest = foundOptionalRequest.get();
            if(foundRequest.getStatus().equals(Status.IN_WORK) || foundRequest.getStatus().equals(Status.WAIT)) {
                model.addAttribute("addRequestError", "Заявка с данной операцией уже подана!");
                return "new-request";
            }
        }

        Request newRequest = new Request();

        newRequest.setStatus(Status.WAIT);
        newRequest.setApplicant(citizenRepository.findById(user.getPersonId()).get());
        newRequest.setOperation(selectOperation);

        requestRepository.save(newRequest);

        model.addAttribute("addRequestMessage", "Заявка успешно добавлена!");

        return "new-request";
    }
}
