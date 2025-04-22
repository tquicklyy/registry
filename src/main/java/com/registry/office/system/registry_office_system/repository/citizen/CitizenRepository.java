package com.registry.office.system.registry_office_system.repository.citizen;

import com.registry.office.system.registry_office_system.entity.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CitizenRepository extends JpaRepository<Citizen, Integer> {
    Optional<Citizen> findById(int id);
}
