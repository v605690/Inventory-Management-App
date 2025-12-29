package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Vendor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VendorService {
    Vendor saveVendor(Vendor vendor);

    List<Vendor> getAllVendors();

}
