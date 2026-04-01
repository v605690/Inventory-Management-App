package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.helpers.AccessHelper;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.VendorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    private static AccessHelper accessHelper;

    public VendorDetailViewController(AccessHelper accessHelper) {
        VendorDetailViewController.accessHelper = accessHelper;
    }


    // List all vendors
    @GetMapping("/all")
    public String listVendors(@RequestParam(name = "contactName", required = false, defaultValue = "") String contactName,
                              @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                              @RequestParam(defaultValue = "0") int page, Model model, Authentication authentication) {

        int size = 22;
        pagination(contactName, keyword, page, model, authentication, size, vendorService);

        return "vendors-list";
    }

    static void pagination(@RequestParam(name = "contactName", required = false, defaultValue = "") String contactName, @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword, @RequestParam(defaultValue = "0") int page, Model model, Authentication authentication, int size, VendorService vendorService) {
        Long userId = accessHelper.getLoggedInUserDetails();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Page<Vendor> vendorPage;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id"));

        if (isAdmin) {
            // Admin Logic: Priority to contactName, then keyword
            if (contactName != null && !contactName.trim().isEmpty()) {
                vendorPage = vendorService.findByContactName(contactName, pageRequest);
            } else {
                vendorPage = vendorService.getAllVendors(keyword, pageRequest);
            }
        } else {
            // User Logic: Filter by userId + search criteria
            if (contactName != null && !contactName.trim().isEmpty()) {
                // Your existing method for specific search
                vendorPage = vendorService.findByCreatedByUserIdAndContactNameContainingIgnoreCase(userId, contactName, pageRequest);
            } else {
                // Default: List all vendors owned by user
                vendorPage = vendorService.findByCreatedByUserId(userId, pageRequest);
            }
        }
            model.addAttribute("vendors", vendorPage.getContent());
            model.addAttribute("searchResults", vendorPage);
            model.addAttribute("totalPages", vendorPage.getTotalPages());
            model.addAttribute("contactName", contactName);
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", Sort.by("id"));
        }

        // Show vendor details and associated products
        @GetMapping("/{accountNumber}")
        public String showVendorDetails (@PathVariable String accountNumber, Model model){
            Vendor vendor = vendorService.findByAccountNumberWithProducts(accountNumber);

            ProductDTO productDTO = new ProductDTO();

            model.addAttribute("vendor", vendor);
            model.addAttribute("product", productDTO);

            return "vendor-details";
        }

        // Search for existing products to associate
        @GetMapping("/{id}/search-products")
        public String searchProducts (@PathVariable Long id, @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page, Model model){

            int size = 27;
            Page<Product> productPage = vendorService.searchProducts(keyword, PageRequest.of(page, size, Sort.by("id")));

            model.addAttribute("id", id);
            model.addAttribute("vendor", vendorService.getVendor(id));
            model.addAttribute("searchResults", productPage);
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);
            model.addAttribute("sortBy", Sort.by("id"));
            model.addAttribute("totalPages", productPage.getTotalPages());
            // ThymeLeaf requires an initialized (empty) object to bind the input fields
            model.addAttribute("product", new ProductDTO());
            return "vendor-details";
        }

        // Associate a searched product
        @PostMapping("/{vId}/associate/{pId}")
        public String associateProduct (HttpServletRequest request, @PathVariable Long vId, @PathVariable Long pId){
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

            // Allows staying on previous page
            String referer = request.getHeader("Referer");

            return "redirect:" + referer;

        }

        // Associate a new product
        @PostMapping("/{id}/new-product")
        public String associateNewProduct (@PathVariable Long id, @ModelAttribute ProductDTO product){
            vendorService.createNewProductAndAssociate(id, product);
            Vendor vendor = vendorService.getVendor(id);
            return "redirect:/vendors/" + vendor.getAccountNumber();
        }
    }
