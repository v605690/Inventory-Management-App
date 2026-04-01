package com.crus.Inventory_Management_System.helpers;

import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
        return userRepository.findByUsername(authentication.getName())
                .map(User::getUserId)
                .orElse(null);
    }
}
