package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.exceptions.APIException;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Vendor saveVendor(Vendor vendor) {
        Vendor vendorList = modelMapper.map(vendor, Vendor.class);
        Vendor vendorFromDB = vendorRepository.findVendorByAccountNumber(vendor.getAccountNumber());
        if (vendorFromDB != null) {
            throw new APIException("Vendor with account number " + vendor.getAccountNumber() + " already exists");
        }
        Vendor savedVendor = vendorRepository.save(vendorList);
        return modelMapper.map(savedVendor, Vendor.class);
    }

    @Override
    public List<Vendor> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();

        if (vendors.isEmpty()) {
            throw new APIException("No vendors found");
        }
        return vendors;
    }
}
