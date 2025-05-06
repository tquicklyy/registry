package com.registry.office.system.registry_office_system.repository.divorceRegistration;

import com.registry.office.system.registry_office_system.entity.DivorceRegistration;
import com.registry.office.system.registry_office_system.entity.MarriageRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DivorceRegistrationRepository extends JpaRepository<DivorceRegistration, Integer> {
    Optional<DivorceRegistration> findByMarriageRegistration(MarriageRegistration marriageRegistration);
}