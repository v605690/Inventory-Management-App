package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
