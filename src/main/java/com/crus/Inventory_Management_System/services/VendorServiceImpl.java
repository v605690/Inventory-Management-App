package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.exceptions.APIException;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
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
    @Transactional
    public Vendor addVendor(Vendor vendor) {
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

    @Override
    @Transactional
    public void deleteVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Vendor", "vendorId", vendorId));

        vendorRepository.delete(vendor);

    }

    @Override
    @Transactional
    public void savedVendor(Vendor vendor) {
        // This method is only for creating new vendors
        // Check if ID is not 0, which would indicate an update attempt
        if (vendor.getId() != 0) {
            throw new APIException("Cannot create vendor with existing ID. Use update method instead.");
        }
        vendorRepository.save(vendor);
    }

    @Override
    @Transactional
    public Vendor updateVendor(Vendor vendor, String accountNumber) {
        // Ensure we have a valid ID for update
        if (vendor.getId() == 0) {
            throw new APIException("Cannot update vendor without a valid ID.");
        }

        Vendor savedVendor = vendorRepository.findById(vendor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", vendor.getId()));

        // Only update if account number hasn't changed, or if new account number doesn't exist
        if (!savedVendor.getAccountNumber().equals(vendor.getAccountNumber())) {
            Vendor existingVendor = vendorRepository.findVendorByAccountNumber(vendor.getAccountNumber());
            if (existingVendor != null && existingVendor.getId() != vendor.getId()) {
                throw new APIException("Vendor with account number " + vendor.getAccountNumber() + " already exists");
            }
        }

        savedVendor.setAccountNumber(vendor.getAccountNumber());
        savedVendor.setAddress(vendor.getAddress());
        savedVendor.setContactName(vendor.getContactName());
        savedVendor.setPhoneNumber(vendor.getPhoneNumber());
        savedVendor.setEmailAddress(vendor.getEmailAddress());

        return vendorRepository.save(savedVendor);
    }
}
