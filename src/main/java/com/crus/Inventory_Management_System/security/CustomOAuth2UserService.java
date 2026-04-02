package com.crus.Inventory_Management_System.security;

import com.crus.Inventory_Management_System.entity.Role;
import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.repositories.RoleRepository;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        System.out.println(oAuth2User.getAttributes());

        String registrationId = request.getClientRegistration().getRegistrationId();
        System.out.println("Provider = " + registrationId);
        System.out.println("Attributes = " + oAuth2User.getAttributes());

        String username = extractUsername(oAuth2User, registrationId);
        String email = extractEmail(oAuth2User, registrationId, username);

        User user = userRepository.findByUsername(username)
                .orElseGet(() -> createNewUser(username, email));

        user.setEmail(email);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);

        User saveduser = userRepository.save(user);
        return new CustomOAuth2User(oAuth2User, saveduser);
    }

    @Transactional
    protected User createNewUser(String username, String email) {
        Role userRole = roleRepository.findByRole(Role.Roles.ROLE_USER);
        if (userRole == null) {
            throw new IllegalStateException("ROLE_USER is missing from database");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("OAUTH2_LOGIN");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.getAuthorities().add(userRole);

        return user;
    }

    private String extractEmail(OAuth2User oAuth2User, String registrationId, String username) {
        if ("github".equalsIgnoreCase(registrationId)) {
            Object email = oAuth2User.getAttribute("email");
            if (email != null) {
                return String.valueOf(email);
            }
            return username + "@github.local";
        }
        Object email = oAuth2User.getAttributes().get("email");
        return email != null ? String.valueOf(email) : username + "@unknown.local";
    }

    private String extractUsername(OAuth2User oAuth2User, String registrationId) {
        if ("github".equalsIgnoreCase(registrationId)) {
            Object email = oAuth2User.getAttributes().get("email");
            if (email != null) {
                return String.valueOf(email);
            }
            Object login = oAuth2User.getAttributes().get("login");
            if (login != null) {
                return String.valueOf(login);
            }
            return oAuth2User.getAttribute("login");
        }
        Object login = oAuth2User.getAttributes().get("login");
        if (login != null) {
            return String.valueOf(login);
        }
        return oAuth2User.getAttribute("login");
    }
}


