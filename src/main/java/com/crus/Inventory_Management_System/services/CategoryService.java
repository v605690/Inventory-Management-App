package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Category;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    public Category parseCategory(String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty() || "null".equalsIgnoreCase(categoryName.trim())) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }

        String normalizeName = categoryName.trim().toUpperCase()
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");

        System.out.println("RAW CATEGORY = [" + categoryName + "]");
        System.out.println("NORMALIZED CATEGORY = [" + normalizeName + "]");

        for (Category category : Category.values()) {
            if (category.name().equals(normalizeName)) {
                return category;
            }
        }

        String availableCategories = Arrays.stream(Category.values())
                .map(Category::name)
                .map(name -> name.replace("_", " "))
                .collect(Collectors.joining(", "));

        throw new IllegalArgumentException(String.format("Invalid category '%s'.  Available categories are: %s",
                categoryName, availableCategories));
    }
}
