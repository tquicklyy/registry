
package com.registry.office.system.registry_office_system.repository.marriageRegistration;

import com.registry.office.system.registry_office_system.entity.Citizen;
import com.registry.office.system.registry_office_system.entity.MarriageRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarriageRegistrationRepository extends JpaRepository<MarriageRegistration, Integer> {
    Optional<MarriageRegistration> findByHusband(Citizen husband);
    Optional<MarriageRegistration> findByWife(Citizen wife);
}
