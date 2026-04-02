package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(Role.Roles roles);
}
