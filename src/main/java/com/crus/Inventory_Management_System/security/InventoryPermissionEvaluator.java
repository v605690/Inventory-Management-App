package com.crus.Inventory_Management_System.security;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Role;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

@Component
public class InventoryPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    VendorRepository vendorRepository;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, @NonNull Object permission) {

        if (!permission.getClass().equals("".getClass())) {
            throw new SecurityException("Cannot execute hasPermission() calls where " +
                                                "permission is not in String form");
        }

        if (userIsAdmin(authentication)) {
            return true;
        }

        String username = authentication.getName();

            if (targetType.equalsIgnoreCase("product")) {
                Optional<Product> product = productRepository.findById(Long.parseLong(targetId.toString()));

                if (product.isEmpty()) {
                    throw new EntityNotFoundException("The product you are trying to access does not exist");
                }

                return product
                        .get().getUser() != null
                        && product.get().getUser().getEmail().equalsIgnoreCase(username);
            }
            else if (targetType.equalsIgnoreCase("vendor")) {
                Optional<Vendor> vendor = vendorRepository.findById(Long.parseLong(targetId.toString()));

                if (vendor.isEmpty()) {
                    throw new EntityNotFoundException("The vendor you are trying to access does not exist");
                }
                return vendor
                        .get().getEmailAddress() != null
                        && vendor.get().getEmailAddress().equalsIgnoreCase(username);
            }
            return false;
        }
    private boolean userIsAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
