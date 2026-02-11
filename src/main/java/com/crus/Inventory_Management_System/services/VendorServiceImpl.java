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
import java.util.Optional;

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
        Optional<Vendor> vendorFromDB = vendorRepository.findVendorByAccountNumber(vendor.getAccountNumber());
        if (vendorFromDB != null) {
            throw new APIException("Vendor with account number " + vendor.getAccountNumber() + " already exists");
        }
        Vendor savedVendor = vendorRepository.save(vendorList);
        return modelMapper.map(savedVendor, Vendor.class);
    }

    @Override
    public List<Vendor> getAllVendors() {
      return vendorRepository.findAllWithProducts();
    }

    @Override
    public Vendor getVendor(Long vendorId) {
        return null;
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
        vendorRepository.save(vendor);
    }

    @Override
    @Transactional
    public Vendor updateVendor(Vendor vendor, String accountNumber) {
        Vendor savedVendor = vendorRepository.findById(vendor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", vendor.getId()));

        savedVendor.setAccountNumber(vendor.getAccountNumber());
        savedVendor.setAddress(vendor.getAddress());
        savedVendor.setContactName(vendor.getContactName());
        savedVendor.setPhoneNumber(vendor.getPhoneNumber());
        savedVendor.setEmailAddress(vendor.getEmailAddress());

        savedVendor.getProducts().clear();
        if (vendor.getProducts() != null) {
            savedVendor.getProducts().addAll(vendor.getProducts());
        }

        return vendorRepository.save(savedVendor);
    }

    @Override
    public List<Vendor> saveAllVendor(List<Vendor> vendorList) {
        return List.of();
    }

    @Override
    public Vendor findVendorByAccountNumber(String accountNumber) {

        return vendorRepository.findVendorByAccountNumber(accountNumber)
                .orElseThrow(() -> new APIException("Vendor not found"));

    }
}
