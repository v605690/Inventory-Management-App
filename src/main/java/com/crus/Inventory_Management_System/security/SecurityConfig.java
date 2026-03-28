package com.crus.Inventory_Management_System.security;//package com.crus.Inventory_Management_System.security;

import com.crus.Inventory_Management_System.services.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomOidcUserService customOidcUserService) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOidcUserService = customOidcUserService;
    }

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.DELETE, "/products/delete/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/products/edit/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/graph/**", "/graph").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/meatPrice/**", "/meatPrice").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/price/**", "/price").hasRole("ADMIN")

                        .requestMatchers("/login/**", "/register", "/register/**", "/overview", "/overview/**").permitAll()
                        .requestMatchers("/", "/webjars/**", "/css/**", "/js/**", "/images/**", "/product-images/**", "/w3images/**",
                                "/favicon.ico", "/login", "/oauth2/**", "/error").permitAll()

                        .requestMatchers(HttpMethod.GET, "/index/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/**", "/api").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/**", "/api").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vendors/**", "/vendors").authenticated()
                        .requestMatchers(HttpMethod.GET, "/vendors/all", "/vendors/all/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/vendors-list/**", "/vendors-list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/getVendors/**", "/getVendors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").authenticated()
                        .requestMatchers(HttpMethod.POST, "/products/new").authenticated()
                        .requestMatchers(HttpMethod.GET, "/products/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/products/keyword/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/products/keyword").authenticated()

                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())


                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(authorities -> {
                                    List<GrantedAuthority> mapped = new ArrayList<>(authorities);
                                    mapped.add(new SimpleGrantedAuthority("ROLE_USER"));
                                    return mapped;
                                })

                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService)
                        )
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}