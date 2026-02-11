package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendors")
public class VendorDetailViewController {

    @Autowired
    VendorService vendorService;

    @Autowired
    ProductService productService;

    @GetMapping("/{accountNumber}")
    public String getVendor(@PathVariable String accountNumber, Model model) {
        Vendor vendor = vendorService.findVendorByAccountNumber(accountNumber);
        model.addAttribute("vendor", vendor);
        return "vendor-detail";
    }
}
