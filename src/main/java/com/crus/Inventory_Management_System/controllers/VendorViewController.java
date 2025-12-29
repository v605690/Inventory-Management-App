package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class VendorViewController {

    @Autowired
    VendorService vendorService;

    @PostMapping("/saveVendor")
    public String saveVendor(@ModelAttribute("vendor") Vendor vendor, Model model) {
        model.addAttribute("vendor", vendor);

    vendorService.saveVendor(vendor);
    return "redirect:/vendors";
    }

    @GetMapping("/vendors")
    public String getAllVendors(Model model) {
        List<Vendor> vendors = vendorService.getAllVendors();
        System.out.println("Vendors found: " + vendors.size());
        model.addAttribute("vendors", vendors);
        return "vendors";
    }
}
