package com.crus.Inventory_Management_System.helpers;

import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccessHelper {

    private final UserRepository userRepository;

    public AccessHelper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getLoggedInUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return null;
            }

                Object principal = authentication.getPrincipal();

                if (principal instanceof User user) {
                    return user.getUserId();
                }
                String username = authentication.getName();
                return userRepository.findByUsername(username)
                        .map(User::getUserId)
                        .orElse(null);
    }
}
