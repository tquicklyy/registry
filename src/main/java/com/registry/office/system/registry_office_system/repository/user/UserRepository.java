package com.registry.office.system.registry_office_system.repository.user;

import com.registry.office.system.registry_office_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}