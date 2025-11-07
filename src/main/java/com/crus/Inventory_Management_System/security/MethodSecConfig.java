package com.crus.Inventory_Management_System.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity
public class MethodSecConfig {

    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
            InventoryPermissionEvaluator inventoryPermissionEvaluator) {

        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();

        handler.setPermissionEvaluator(inventoryPermissionEvaluator);
        return handler;
    }
}
