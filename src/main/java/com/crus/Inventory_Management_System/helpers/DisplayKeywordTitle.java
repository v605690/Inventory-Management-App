package com.crus.Inventory_Management_System.helpers;

import org.springframework.stereotype.Component;

@Component
public class DisplayKeywordTitle {

    public DisplayKeywordTitle() {
    }

    public String displayTitle(String category, String keyword) {

        return "Search results for: " + toDisplayTitle(category, keyword);
    }

    private static String toDisplayTitle(String category, String keyword) {
        String displayKeyword = (keyword == null) ? "" : keyword.trim();

        if (!displayKeyword.isEmpty()) {
            return displayKeyword.substring(0, 1).toUpperCase() + displayKeyword.substring(1);
        }

        String displayCategory = (category == null) ? "" : category.trim();

        // Treat "all", "null", or empty as showing all products
        if (displayCategory.isEmpty() || "all".equalsIgnoreCase(displayCategory) || "null".equalsIgnoreCase(displayCategory)) {
            return "HKM Products";
        }

        return displayCategory.substring(0, 1).toUpperCase() + displayCategory.substring(1);
    }
}
