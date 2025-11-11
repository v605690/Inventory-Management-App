package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/calculate")
public class CategoryPriceController {

    @Autowired
    private CategoryPriceService categoryPriceService;

    @GetMapping("/categories/{categoryName}/price")
    public ResponseEntity<CategoryPriceDTO> calculateTotalPriceByRetailPrice(@PathVariable String categoryName) throws ResourceNotFoundException {
        CategoryPriceDTO totalPrice = categoryPriceService.calculateCategoryTotalPrice(categoryName);

        return ResponseEntity.ok(totalPrice);
    }
}
