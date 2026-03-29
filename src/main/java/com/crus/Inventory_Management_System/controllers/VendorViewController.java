package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vendors")
public class VendorViewController {

    @Autowired
    VendorService vendorService;

    @Autowired
    ProductService productService;
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
    public String saveVendor(@Valid @ModelAttribute("vendor") Vendor vendor, Model model, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "22") int size) {
        try {
            vendorService.savedVendor(vendor);
            System.out.println("Vendor Saved: " + vendor);
            return "redirect:/vendors";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save vendor: " + e.getMessage());
            model.addAttribute("vendor", vendor);
            model.addAttribute("vendors", vendorService.getAllVendors(keyword, PageRequest.of(page, size)));
            return "vendors";
        }
    }

    @PostMapping("/update")
    public String updateVendor(@Valid @ModelAttribute("vendor") Vendor vendor, Model model, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "22") int size) {
        if (vendor == null ||
            vendor.getAccountNumber() == null || vendor.getAccountNumber().isEmpty() ||
            vendor.getContactName() == null || vendor.getContactName().isEmpty() ||
            vendor.getAddress() == null || vendor.getAddress().isEmpty() ||
            vendor.getPhoneNumber() == null || vendor.getPhoneNumber().isEmpty() ||
            vendor.getEmailAddress() == null || vendor.getEmailAddress().isEmpty()) {

            model.addAttribute("message", "Please fill in all required fields correctly.");
            model.addAttribute("vendors", vendorService.getAllVendors(keyword, PageRequest.of(page, size)));

            return "vendors";
        }

        vendorService.updateVendor(vendor, vendor.getAccountNumber());
        return "redirect:/vendors";
    }


    @GetMapping()
    public String getAllVendors(Model model, Authentication authentication, @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "22") int size) {
        Page<Vendor> vendors = vendorService.getAllVendors(keyword, PageRequest.of(page, size));

        String currentUserId = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Vendor> vendorList;
        if (isAdmin) {
            vendorList = new ArrayList<>(vendors.getContent());
        } else {
            vendorList = vendors.getContent().stream()
                    .filter(vendor -> false)
                    .toList();
        }

        model.addAttribute("vendors", vendorList);
        model.addAttribute("searchResults", vendors);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vendors.getTotalPages());

        return "vendors";
    }

    @GetMapping("/delete/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return "redirect:/vendors";
    }
}
