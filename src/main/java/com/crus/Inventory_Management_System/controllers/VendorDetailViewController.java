package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendors")
public class VendorDetailViewController {

    @Autowired
    VendorService vendorService;

    @Autowired
    ProductService productService;

    // List all vendors
    @GetMapping("/all")
    public String listVendors(Model model) {
        model.addAttribute("vendors", vendorService.getAllVendors());
        return "vendors-list";
    }

    // Show vendor details and associated products
     @GetMapping("/{accountNumber}")
    public String showVendorDetails(@PathVariable String accountNumber, Model model) {
        Vendor vendor = vendorService.findVendorByAccountNumber(accountNumber);
        model.addAttribute("vendor", vendor);
        return "vendor-details";
    }

    // Search for existing products to associate
    @GetMapping("/{id}/search-products")
    public String searchProducts(@PathVariable Long id, @RequestParam String keyword, Model model) {
        model.addAttribute("vendor", vendorService.getVendor(id));
        model.addAttribute("searchResults", vendorService.searchProducts(keyword));
        return "vendor-details";
    }

    // Associate a searched product
    @PostMapping("/{vendorId}/associate/{productId}")
    public String associateProduct(@PathVariable Long vendorId, @PathVariable Long productId) {
        vendorService.associateProduct(vendorId, productId);
        return "redirect:/vendors-list" + vendorId;
    }

    // Associate a new product
    @PostMapping("/{id}/new-product")
    public String associateNewProduct(@PathVariable Long id, @ModelAttribute Product product) {
        vendorService.createNewProductAndAssociate(id, product);
        return "redirect:/vendors-list" + id;
    }
}
