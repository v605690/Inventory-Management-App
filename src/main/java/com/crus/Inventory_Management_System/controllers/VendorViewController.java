package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.services.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/vendors")
public class VendorViewController {

    @Autowired
    VendorService vendorService;
// delete after validating if method is not used
//    @GetMapping("/new")
//    public String showCreateForm(Model model) {
//        // This is the crucial part!
//        // We provide an empty object so Thymeleaf has something to bind to.
//        model.addAttribute("vendor", new Vendor());
//        return "vendors";
//    }

    @GetMapping("/new")
    public String showAddVendorForm(Model model) {
        model.addAttribute("vendor", new Vendor());
        return "new-vendor";
    }

    @PostMapping()
    public String saveVendor(@Valid @ModelAttribute("vendor") Vendor vendor, Model model) {
        try {
            vendorService.savedVendor(vendor);
            System.out.println("Vendor Saved: " + vendor);
            return "redirect:/vendors";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save vendor: " + e.getMessage());
            model.addAttribute("vendor", vendor);
            model.addAttribute("vendors", vendorService.getAllVendors());
            return "vendors";
        }
    }

    @PostMapping("/update")
    public String updateVendor(@Valid @ModelAttribute("vendor") Vendor vendor, Model model) {
        if (vendor == null ||
            vendor.getAccountNumber() == null || vendor.getAccountNumber().isEmpty() ||
            vendor.getContactName() == null || vendor.getContactName().isEmpty() ||
            vendor.getAddress() == null || vendor.getAddress().isEmpty() ||
            vendor.getPhoneNumber() == null || vendor.getPhoneNumber().isEmpty() ||
            vendor.getEmailAddress() == null || vendor.getEmailAddress().isEmpty()) {

            model.addAttribute("message", "Please fill in all required fields correctly.");
            model.addAttribute("vendors", vendorService.getAllVendors());
            return "vendors";
        }

        vendorService.updateVendor(vendor, vendor.getAccountNumber());
        return "redirect:/vendors";
    }

    @GetMapping()
    public String getAllVendors(Model model) {
        List<Vendor> vendors = vendorService.getAllVendors();
        System.out.println("Vendors found: " + vendors.size());
        model.addAttribute("vendors", vendors);
        return "vendors";
    }

    @GetMapping("/delete/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return "redirect:/vendors";
    }
}
