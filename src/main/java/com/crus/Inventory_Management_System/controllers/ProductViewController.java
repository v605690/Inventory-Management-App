package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.config.AppConstants;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.services.CategoryServicePriceImpl;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductViewController {

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    CategoryServicePriceImpl categoryServiceImpl;
    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String viewIndexPage() {
        return "index";
    }

    @GetMapping("/products")
    public String viewHomePage(Model model,
                               @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                               @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {

        ProductResponse productResponse = productServiceImpl.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        final List<ProductDTO> productDTOList = productResponse.getContent();

        model.addAttribute("productDTOList", productDTOList);

        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("totalPages", productResponse.getTotalPages());
        model.addAttribute("currentPage", productResponse.getPageNumber());
        model.addAttribute("totalElements", productResponse.getTotalElements());

        return "products";
    }

    @GetMapping("/products/categories/{categoryName}")
    public String viewCategoryPage(Model model,
                                   @PathVariable String categoryName,
                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        List<ProductDTO> productDTOList = productServiceImpl.getProductsByCategory(categoryName, pageNumber, pageSize, sortBy, sortOrder).getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("currentCategory", categoryName);

        return "products";
    }
    @GetMapping("products/keyword")
    public String searchProductByKeyword(Model model, @RequestParam("q") String keyword) {
        final List<ProductDTO> productDTOList = productServiceImpl.getProductByKeyword(keyword).getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("keyword", keyword);

        return "products";
    }

    @GetMapping("/graph")
    public String viewGraphPage() {
        return "graph";
    }

    @GetMapping("/meatPrice")
    public String viewMeatPricePage() {
        return "meatPrice";
    }

    @GetMapping("/overview")
    public String viewOverView() {
        return "overview";
    }

    @GetMapping("/new")
    public String showNewProductPage(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("product", productDTO);

        return "new-product";
    }

    @PostMapping(value = "/save")
    public String saveProduct(@ModelAttribute("product") Product product, Model model) {
        if (product == null) {
            model.addAttribute("message", "Product cannot be null");

            return "error";
        }
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            model.addAttribute("message", "Product name cannot be empty");

            return "error";
        }

        productService.saveProduct(product);
        return "redirect:/new-product";
    }
}
