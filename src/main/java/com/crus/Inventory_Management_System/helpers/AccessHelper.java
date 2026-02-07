package com.crus.Inventory_Management_System.helpers;

import com.crus.Inventory_Management_System.entity.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AccessHelper {

    public Long getLoggedInUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long currentUserId = null;
        if ((authentication != null) && !(authentication instanceof AnonymousAuthenticationToken)) {
            boolean isNotAdmin = authentication.getAuthorities().stream().noneMatch(authority -> authority.getAuthority().equals("ADMIN"));

            if (isNotAdmin) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof User user) {
                    currentUserId = user.getUserId();
                }
            }
        }
        return currentUserId;
    }
}
