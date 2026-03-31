package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
    Optional<Vendor> findVendorByAccountNumber(String accountNumber);

    @Query("select distinct v from Vendor v left join fetch v.products")
    Page<Vendor> findAllWithProducts(Pageable pageable);

    @Query("SELECT v FROM Vendor v LEFT JOIN FETCH v.products WHERE v.accountNumber = :acc")
    Optional<Vendor> findByAccountNumberWithProducts(@Param("acc") String accountNumber);

    List<Vendor> findByContactNameContainingIgnoreCase(String contactName);

    Page<Vendor> findByContactNameContainingIgnoreCase(String contactName, Pageable pageable);
}
