package com.crus.Inventory_Management_System.config;

import com.crus.Inventory_Management_System.entity.Category;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public Category parseCategory(String categoryName) {
        String normalizeName = categoryName.trim().toUpperCase()
                .replace("-", "_")
                .replace(" ", "_")
                .replace("&", "_");

        for (Category category : Category.values()) {
            if (category.name().equals(normalizeName)) {
                return category;
            }
        }

        String availableCategories = Arrays.stream(Category.values())
                .map(Category::name)
                .collect(Collectors.joining(", "));
        throw new IllegalArgumentException(String.format("Invalid category '%s'.  Available categories are: %s",
                categoryName, availableCategories));
    }

}
