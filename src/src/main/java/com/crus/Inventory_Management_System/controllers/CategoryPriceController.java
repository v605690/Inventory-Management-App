package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/calculate")
public class CategoryPriceController {

    @Autowired
    private CategoryPriceService categoryPriceService;

    @GetMapping("/categories/{categoryName}/price")
    public ResponseEntity<CategoryPriceDTO> calculateTotalPriceByRetailPrice(@PathVariable String categoryName, Authentication authentication, Pageable pageable) throws ResourceNotFoundException {
        User user = (User) authentication.getPrincipal();

        CategoryPriceDTO totalPrice = categoryPriceService.calculateCategoryTotalPrice(categoryName, user.getUserId(), pageable);

        return ResponseEntity.ok(totalPrice);
    }
}
