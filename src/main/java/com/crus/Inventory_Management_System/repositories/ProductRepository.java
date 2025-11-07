package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVendorsId(Long id);
    Optional<Product> findByVendorsEmailAddress(String vendors_emailAddress);
    List<Product> findByVendorsContactName(String vendors_contactName);
    List<Product> findByVendorsPhoneNumber(Integer vendors_phoneNumber);
}
