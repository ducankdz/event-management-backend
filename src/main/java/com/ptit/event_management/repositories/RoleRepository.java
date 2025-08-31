package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
