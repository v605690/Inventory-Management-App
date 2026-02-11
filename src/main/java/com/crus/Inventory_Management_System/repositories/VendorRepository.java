package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findVendorByAccountNumber(String accountNumber);

    @Query("select distinct v from Vendor v left join fetch v.products")
    List<Vendor> findAllWithProducts();
}
