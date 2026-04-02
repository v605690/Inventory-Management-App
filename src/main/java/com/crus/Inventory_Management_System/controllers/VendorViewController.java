package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.helpers.AccessHelper;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/vendors")
public class VendorViewController {

    @Autowired
    VendorService vendorService;

    @Autowired
    ProductService productService;

    @Autowired
    private AccessHelper accessHelper;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

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

    @GetMapping("/search")
    public String searchVendors(@RequestParam(name = "contactName") String contactName,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "22") int size,
                                Model model, Authentication authentication) {

        Long userId = accessHelper.getLoggedInUserDetails();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));

        Page<Vendor> vendorPage;

        if (isAdmin) {
            vendorPage = vendorService.findByContactName(contactName, pageRequest);
        } else {
            vendorPage = vendorService.findByCreatedByUserIdAndContactNameContainingIgnoreCase(userId, contactName, pageRequest);
        }

        model.addAttribute("vendors", vendorPage.getContent());
        model.addAttribute("vendorContactList", contactName);
        model.addAttribute("searchResults", vendorPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", vendorPage.getTotalPages());

        return "vendors";
    }

    @PostMapping()
    public String saveVendor(@Valid @ModelAttribute("vendor") Vendor vendor,
                             Model model, String keyword,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "22") int size, Pageable pageable) {
        try {
            Long userId = accessHelper.getLoggedInUserDetails();

            User user = new User();
            user.setUserId(userId);
            vendor.setCreatedBy(user);

            vendorService.savedVendor(vendor);
            System.out.println("Vendor Saved: " + vendor);
            return "redirect:/vendors";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to save vendor: " + e.getMessage());
            model.addAttribute("vendor", vendor);
            model.addAttribute("vendors", vendorService.getAllVendors(keyword, pageable));
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
    public String getAllVendors(Model model, Authentication authentication, Long userId,
                                @RequestParam(name = "contactName", required = false, defaultValue = "") String contactName,
                                @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                                @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "22") int size) {

        VendorDetailViewController.pagination(contactName, keyword, page, model, authentication, size, vendorService);

        return "vendors";
    }

    @GetMapping("/delete/{id}")
    public String deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return "redirect:/vendors";
    }

    @GetMapping("{id}/remove/{productId}")
    public String disassociateVendor(@PathVariable Long id, @PathVariable Long productId) {
        vendorService.disassociateProduct(id, productId);
        return "redirect:/vendors";
    }
}
