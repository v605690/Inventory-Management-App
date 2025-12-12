package com.crus.Inventory_Management_System.helpers;

import org.springframework.context.annotation.Configuration;

@Configuration
public class DisplayKeywordTitle {

    public Object displayTitle(String keyword) {

        return "Search results for: " + toDisplayTitle(keyword);
    }
    private static String toDisplayTitle(String keyword) {

        String displayKeyword = (keyword == null) ? "" : keyword.trim();

        if (displayKeyword.isEmpty()) {
            return "(No Keyword Entered)";
        }

        return displayKeyword.substring(0, 1).toUpperCase() + displayKeyword.substring(1);
    }
}
