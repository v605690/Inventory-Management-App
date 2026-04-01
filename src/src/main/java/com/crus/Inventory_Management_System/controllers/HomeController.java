package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CategoryPriceService categoryPriceService;

    @GetMapping("/")
    public String viewIndexPage() {
        return "index";
    }

    @GetMapping("/overview")
    public String overview() {
        return "overview";
    }

    @GetMapping("/graph")
    public String viewGraphPage(Model model, Pageable pageable) {
        // The code calls a method in the categoryPriceService to fetch a list of all categories
        // and their associated product counts, applying any pagination rules specified in the pageable argument.
        List<CategoryPriceDTO> categoryData = categoryPriceService.getAllCategoryPrices(pageable);

        // The code initializes a new list (graphData) designed to hold the data in a specific 2D format suitable
        // for a charting library (likely JavaScript on the front end). It iterates through the retrieved categoryData
        List<List<Object>> graphData = new ArrayList<>();

        if (categoryData != null) {
            // It retrieves the category name and count from each DTO
            for (CategoryPriceDTO dto : categoryData) {
                String name = dto.getCategoryName();
                int count = (dto.getProductCount() != null) ? dto.getProductCount() : 0;

                // It applies a filter: a category is only added to graphData if its product count is between 0 and 100,000 (inclusive).
                if (count >= 0 && count <= 100000) {
                    graphData.add(Arrays.asList(name, count));
                }
            }
        }
        // The prepared graphData is placed into the Model under the attribute name "chartData".
        // This makes the data accessible within the view template using that name.
        model.addAttribute("chartData", graphData);
        return "category";
    }

    @GetMapping("/meatPrice")
    public String viewMeatPricePage() {
        return "meatPrice";
    }
}
