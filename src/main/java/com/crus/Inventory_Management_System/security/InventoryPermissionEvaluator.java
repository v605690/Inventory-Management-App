package com.crus.Inventory_Management_System.security;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Role;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
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
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {

        if (!permission.getClass().equals("".getClass())) {
            throw new SecurityException("Cannot execute hasPermission() calls where " +
                                                "permission is not in String form");
        }

        if (userIsAdmin(authentication)) {
            return true;
        } else {
            Product productDetails = (Product) authentication.getPrincipal();

            if (targetType.equalsIgnoreCase("product")) {
                Optional<Product> product = productRepository.findById(Long.parseLong(targetId.toString()));

                if (product.isEmpty()) {
                    return true;
                }

                return product
                        .get()
                        .getProductName()
                        .equals(productDetails.getProductName());
            }
        }
        return false;
    }

    private boolean userIsAdmin(Authentication authentication) {
        Collection<Role> grantedAuthorities = (Collection<Role>) authentication.getAuthorities();

        for (Role r : grantedAuthorities) {
            if (r.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
