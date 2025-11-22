package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/calculate")
public class CategoryPriceController {

    @Autowired
    private CategoryPriceService categoryPriceService;

    @GetMapping("/categories/{categoryName}/price")
    public ResponseEntity<CategoryPriceDTO> calculateTotalPriceByRetailPrice(@PathVariable String categoryName, Pageable pageable) throws ResourceNotFoundException {
        CategoryPriceDTO totalPrice = categoryPriceService.calculateCategoryTotalPrice(categoryName, pageable);

        return ResponseEntity.ok(totalPrice);
    }

    @GetMapping("/price")
    public String showPricePage(Model model, Pageable pageable) {
        List<CategoryPriceDTO> priceDTOS = categoryPriceService.getAllCategoryPrices(pageable);

        model.addAttribute("priceDTOS", priceDTOS);

        return "price";
    }
}
