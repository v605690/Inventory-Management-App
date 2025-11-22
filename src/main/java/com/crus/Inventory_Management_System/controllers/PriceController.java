package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class PriceController {

    @Autowired
    CategoryPriceService categoryPriceService;

//    @GetMapping("/price")
//    @ResponseBody // <--- Forces return of text, ignores price.html
//    public String testConnection() {
//        return "Connection Successful! The Controller is working.";
//    }

    @GetMapping("/price")
    public String showPricePage(Model model, Pageable pageable) {
        // Use unpaged to get ALL categories
        List<CategoryPriceDTO> list = categoryPriceService.getAllCategoryPrices(pageable);

        // Debugging: print to IntelliJ console to prove Java is working
        System.out.println("Controller found " + list.size() + " categories.");

        model.addAttribute("tableData", list);

        return "price";
    }
}
