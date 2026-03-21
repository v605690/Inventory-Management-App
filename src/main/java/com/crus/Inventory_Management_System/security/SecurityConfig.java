package com.crus.Inventory_Management_System.security;//package com.crus.Inventory_Management_System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/products/delete/**").hasRole("ADMIN")
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
                        .requestMatchers(HttpMethod.GET, "/vendors/**", "/vendors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vendors-list/**", "/vendors-list").permitAll()
                        .requestMatchers(HttpMethod.GET, "/getVendors/**", "/getVendors").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/keyword/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/keyword").permitAll()

                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll())

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}