package com.registry.office.system.registry_office_system.repository.employee;

import com.registry.office.system.registry_office_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Optional<Employee> findById(int id);
}
