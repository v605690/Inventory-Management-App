package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Vendor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface VendorService {
    Vendor addVendor(Vendor vendor);

    List<Vendor> getAllVendors();

    Vendor getVendor(Long vendorId);

    void deleteVendor(Long vendorId);

    void savedVendor(Vendor vendor);

    Vendor updateVendor(Vendor vendor, String accountNumber);

    List<Vendor> saveAllVendor(List<Vendor> vendorList);
}
