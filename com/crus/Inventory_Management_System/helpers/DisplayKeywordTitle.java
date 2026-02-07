    private static String toDisplayTitle(String category, String keyword) {

        if (category != null && !category.trim().isEmpty()) {
            return category.substring(0, 1).toUpperCase() + category.substring(1);
        }

        String displayKeyword = (keyword == null) ? "" : keyword.trim();

        if (displayKeyword.isEmpty()) {
            return "(No Keyword Entered)";
        }

        return displayKeyword.substring(0, 1).toUpperCase() + displayKeyword.substring(1);
    }
