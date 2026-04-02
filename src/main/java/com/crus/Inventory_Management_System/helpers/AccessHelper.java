package com.crus.Inventory_Management_System.helpers;

import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import com.crus.Inventory_Management_System.security.CustomOAuth2User;
import com.crus.Inventory_Management_System.security.CustomOidCUser;
import com.crus.Inventory_Management_System.security.CustomOidcUserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class AccessHelper {

    public Long getLoggedInUserDetails() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomOidCUser customOidCUser) {
            return customOidCUser.getId();
        }

        if (principal instanceof CustomOAuth2User customOAuth2User) {
            return customOAuth2User.getUserId();
        }

        if (principal instanceof User user) {
            return user.getUserId();
        }
        return null;
    }
}
