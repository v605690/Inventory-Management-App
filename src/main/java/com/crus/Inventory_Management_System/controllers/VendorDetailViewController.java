package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/vendors")
public class VendorDetailViewController {

    @Autowired
    VendorService vendorService;

    @Autowired
    ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private VendorRepository vendorRepository;

    // List all vendors
    @GetMapping("/all")
    public String listVendors(@RequestParam(name = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page, Model model, Authentication authentication) {

        int size = 22;
        Page<Vendor> vendorPage = vendorService.getAllVendors(keyword, PageRequest.of(page, size));

        String currentUserId = authentication.getName();

        boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        List<Vendor> vendorList;
        if (isAdmin) {
            vendorList = new ArrayList<>(vendorPage.getContent());
        } else {
            vendorList = vendorPage.getContent().stream()
                    .filter(vendor -> false)
                    .toList();
        }

        model.addAttribute("vendors",vendorList);
        model.addAttribute("searchResults", vendorPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vendorPage.getTotalPages());

        return "vendors-list";
    }

    // Show vendor details and associated products
     @GetMapping("/{accountNumber}")
    public String showVendorDetails(@PathVariable String accountNumber, Model model) {
        Vendor vendor = vendorService.findByAccountNumberWithProducts(accountNumber);

        ProductDTO productDTO = new ProductDTO();

        model.addAttribute("vendor", vendor);
        model.addAttribute("product", productDTO);

        return "vendor-details";
    }

    // Search for existing products to associate
    @GetMapping("/{id}/search-products")
    public String searchProducts(@PathVariable Long id, @RequestParam String keyword, @RequestParam(defaultValue = "0") int page, Model model) {

        int size = 27;
        Page<Product> productPage = vendorService.searchProducts(keyword, PageRequest.of(page, size));

        model.addAttribute("id", id);
        model.addAttribute("vendor", vendorService.getVendor(id));
        //model.addAttribute("searchResults", vendorService.searchProducts(keyword, PageRequest.of(page, size)));
        model.addAttribute("searchResults", productPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        // ThymeLeaf requires an initialized (empty) object to bind the input fields
        model.addAttribute("product", new ProductDTO());
        return "vendor-details";
    }

    // Associate a searched product
    @PostMapping("/{vId}/associate/{pId}")
    public String associateProduct(@PathVariable Long vId, @PathVariable Long pId) {
        System.out.println(">>> ASSOCIATION START - VendorID: " + vId + " ProductID: " + pId);
        log.info("REACHED CONTROLLER");
        Vendor vendor = vendorService.getVendor(vId);
        try {
            System.out.println("DEBUG: Entering Controller for IDs: " + vId + ", " + pId);
            vendorService.associateProduct(vId, pId);
            System.out.println("DEBUG: Service call finished successfully");
        } catch (Exception e) {
            e.printStackTrace(); // This will show you WHY it's failing in the console
            return "error";
        }

        //Vendor vendor = vendorService.getVendor(vId);
        return "redirect:/vendors/" + vendor.getAccountNumber();
//        return "Product Count in DB: " + vendor.getProducts().size();
    }

    // Associate a new product
    @PostMapping("/{id}/new-product")
    public String associateNewProduct(@PathVariable Long id, @ModelAttribute ProductDTO product) {
        vendorService.createNewProductAndAssociate(id, product);
        Vendor vendor = vendorService.getVendor(id);
        return "redirect:/vendors/" + vendor.getAccountNumber();
    }
}
