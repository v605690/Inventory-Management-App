package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import com.crus.Inventory_Management_System.services.CategoryServicePriceImpl;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class CategoryProductViewController {

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    CategoryServicePriceImpl categoryServiceImpl;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryPriceService categoryPriceService;

    private static final String ALLOWED_CATEGORY = "Bakery";
    private static final String ALLOWED_CATEGORY2 = "bbq";

    @GetMapping("/searchBakery")
    public String searchOnlyBakery(Model model, @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        final List<ProductDTO> productDTOList = productServiceImpl.getProductByKeywordAndCategory(keyword, ALLOWED_CATEGORY).getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryName", ALLOWED_CATEGORY);

        return "product-results";
    }

    @GetMapping("/searchBBQ")
    public String searchOnlyBBQ(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        final List<ProductDTO> productDTOList = productServiceImpl.getProductByKeywordAndCategory(keyword, ALLOWED_CATEGORY2).getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("searchKeyword", keyword);
        model.addAttribute("categoryName", ALLOWED_CATEGORY2);

        return "product-results";
    }
}
