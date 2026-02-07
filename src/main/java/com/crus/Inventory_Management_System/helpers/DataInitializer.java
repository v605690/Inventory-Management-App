package com.crus.Inventory_Management_System.helpers;

import com.crus.Inventory_Management_System.entity.Role;
import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.RoleRepository;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeUsers();
    }

    private void initializeRoles() {

        if (roleRepository.findByRole(Role.Roles.ROLE_ADMIN) == null) {
            Role adminRole = Role.builder()
                    .role(Role.Roles.ROLE_ADMIN)
                    .build();
            roleRepository.save(adminRole);
            log.info("Created ROLE_ADMIN");
        }

        if (roleRepository.findByRole(Role.Roles.ROLE_USER) == null) {
            Role userRole = Role.builder()
                    .role(Role.Roles.ROLE_USER)
                    .build();
            roleRepository.save(userRole);
            log.info("Create ROLE_USER");
        }
    }

    private void initializeUsers() {
        if (userRepository.findByUsername("admin") == null) {
            Role adminRole = roleRepository.findByRole(Role.Roles.ROLE_ADMIN);

            List<Role> adminAuthorities = new ArrayList<>();
            adminAuthorities.add(adminRole);

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("gJq9jNc8mY3WuIo"))
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .isAccountNonExpired(true)
                    .isEnabled(true)
                    .authorities(adminAuthorities)
                    .build();
            userRepository.save(admin);
            log.info("Created admin user with password");
        }

        if (userRepository.findByUsername("user") == null) {
            Role userRole = roleRepository.findByRole(Role.Roles.ROLE_USER);

            List<Role> userAuthorities = new ArrayList<>();
            userAuthorities.add(userRole);

            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("!QAZ2wsx"))
                    .isAccountNonLocked(true)
                    .isCredentialsNonExpired(true)
                    .isAccountNonExpired(true)
                    .isEnabled(true)
                    .authorities(userAuthorities)
                    .build();

            userRepository.save(user);
            log.info("Created regular user with password");
        }
    }
}
