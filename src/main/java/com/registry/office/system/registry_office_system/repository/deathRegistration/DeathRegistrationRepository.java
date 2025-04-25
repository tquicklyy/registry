package com.registry.office.system.registry_office_system.repository.deathRegistration;

import com.registry.office.system.registry_office_system.entity.DeathRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeathRegistrationRepository extends JpaRepository<DeathRegistration, Integer> {
    Optional<DeathRegistration> findById(int id);
}
