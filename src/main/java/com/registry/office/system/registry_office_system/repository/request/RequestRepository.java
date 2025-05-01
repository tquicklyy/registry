
package com.registry.office.system.registry_office_system.repository.request;

import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.Request;
import com.registry.office.system.registry_office_system.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findByStatusAndApplicant(Status status, Citizen applicant);
    List<Request> findByApplicant(Citizen applicant);
    List<Request> findByStatus(Status status);
    Optional<Request> findByVisitDate(LocalDateTime visitDate);
}
