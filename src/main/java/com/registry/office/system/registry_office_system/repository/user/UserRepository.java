package com.registry.office.system.registry_office_system.repository.user;

import com.registry.office.system.registry_office_system.entity.User;
import com.registry.office.system.registry_office_system.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPersonId(int personId);
    Optional<User> findByUsername(String username);
    boolean existsBySnils(String snils);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<User> findBySnils(String snils);
    Optional<User> findByRoleAndPersonId(Role role, int personId);
}