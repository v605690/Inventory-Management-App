package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.exceptions.APIException;
import com.crus.Inventory_Management_System.services.VendorService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class VendorController {

    @Autowired
    private VendorService vendorService;

    @PostMapping("/vendors")
    public ResponseEntity<?> saveVendor(@Valid @RequestBody Vendor vendor) {
        log.info("Saving vendor with account number: {}", vendor.getAccountNumber());

        try {
            Vendor savedVendor = vendorService.addVendor(vendor);
            return new ResponseEntity<>(savedVendor, HttpStatus.CREATED);
        } catch (APIException e) {
            log.error("Error saving vendor: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getMyVendors")
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = (vendorService.getAllVendors());
        return new ResponseEntity<>(vendors, HttpStatus.OK);
    }
}
