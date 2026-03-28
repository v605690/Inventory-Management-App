package com.crus.Inventory_Management_System.security;

import com.crus.Inventory_Management_System.entity.Role;
import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.RoleRepository;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest request) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();

        System.out.println("Provider = " + registrationId);
        System.out.println("Attributes = " + oidcUser.getAttributes());

        String username = extractUsername(oidcUser, registrationId);
        String email = extractEmail(oidcUser, registrationId, username);

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> creatNewUser(username, email));

        user.setEmail(email);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        User saveduser = userRepository.save(user);
        return new CustomOidCUser(oidcUser, saveduser);
    }

    @Transactional
    protected User creatNewUser(String username, String email) {
        Role userRole = roleRepository.findByRole(Role.Roles.ROLE_USER);
        if (userRole == null) {
            throw new IllegalStateException("ROLE_USER is missing from database");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("OIDCUSER_LOGIN");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.getAuthorities().add(userRole);

        return user;
    }

    private String extractEmail(OidcUser oidcUser, String registrationId, String username) {
        if ("google".equalsIgnoreCase(registrationId)) {
            Object email = oidcUser.getAttributes().get("email");
            if (email != null) {
                return String.valueOf(email);
            }
            return username + "@google.local";
        }
        Object email = oidcUser.getAttributes().get("email");
        return email != null ? String.valueOf(email) : username + "@unkown.local";
    }

    private String extractUsername(OidcUser oidcUser, String registrationId) {
        if ("google".equalsIgnoreCase(registrationId)) {
            Object email = oidcUser.getAttributes().get("email");
            if (email != null) {
                return String.valueOf(email);
            }
            Object preferredUsername = oidcUser.getAttributes().get("preferred_username");
            if (preferredUsername != null) {
                return String.valueOf(preferredUsername);
            }
            return oidcUser.getSubject();
        }
        Object preferredUsername = oidcUser.getAttributes().get("preferred_username");
        if (preferredUsername != null) {
            return String.valueOf(preferredUsername);
        }
        return oidcUser.getSubject();
    }
}
