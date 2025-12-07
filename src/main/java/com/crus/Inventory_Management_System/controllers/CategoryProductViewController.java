package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.config.AppConstants;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import com.crus.Inventory_Management_System.services.CategoryServicePriceImpl;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class CategoryProductViewController {

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    CategoryServicePriceImpl categoryServiceImpl;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryPriceService categoryPriceService;

    private static final String BAKERY = "Bakery";
    private static final String BBQ = "bbq";
    private static final String BEER_WINE = "beer_wine";
    private static final String COSMETICS = "cosmetics";
    private static final String DAIRY = "dairy";
    private static final String FROZEN_FOODS = "frozen_foods";
    private static final String GROCERY = "grocery";
    private static final String JUICE_COCKTAIL = "juice_cocktail";
    private static final String MEAT = "meat";
    private static final String MEDICINE = "medicine";
    private static final String NON_FOOD = "non_food";
    private static final String ONLINE_LOTTO = "online_lotto";
    private static final String PREPARED_FOOD = "prepared_food";
    private static final String PRODUCE = "produce";
    private static final String SCRATCH_OFFS = "scratch_offs";
    private static final String SEAFOOD = "seafood";
    private static final String SOFT_DRINKS = "soft_drinks";
    private static final String ENERGY_DRINKS = "energy_drinks";
    private static final String TOBACCO = "tobacco";
    private static final String HOT_FOOD = "hot_food";
    private static final String ICECREAM = "icecream";
    private static final String FASHION_WEAR = "fashion_wear";
    private static final String KITCHEN = "kitchen";
    private static final String LIQUOR = "liquor";
    private static final String FROZEN_YOGURT = "frozen_yogurt";
    private static final String DEPARTMENT_NOT_ON_FILE = "department_not_on_file";

    @GetMapping("/searchBakery")
    public String searchOnlyBakery(Model model,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        productItems(model, keyword, pageNumber, pageSize, sortBy, sortOrder, BAKERY);

        return "bakery";
    }

    private void productItems(Model model, @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                              @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                              @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                              @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                              @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder, String bakery) {

        ProductResponse productResponse = productServiceImpl.getProductByKeywordAndCategory(keyword, bakery, pageNumber, pageSize, sortBy, sortOrder);
        final List<ProductDTO> productDTOList = productResponse.getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryName", bakery);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("totalPages", productResponse.getTotalPages());
        model.addAttribute("currentPage", productResponse.getPageNumber());
        model.addAttribute("totalElements", productResponse.getTotalElements());
    }

    @GetMapping("/searchBBQ")
    public String searchOnlyBBQ(Model model,
                                   @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        productItems(model, keyword, pageNumber, pageSize, sortBy, sortOrder, BBQ);

        return "barbecue";
    }

    @GetMapping("/searchBeerWine")
    public String searchOnlyBeerWine(Model model,
                                @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        productItems(model, keyword, pageNumber, pageSize, sortBy, sortOrder, BEER_WINE);

        return "beerWine";
    }

    @GetMapping("/searchCosmetics")
    public String searchOnlyCosmetics(Model model,
                                     @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                     @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                     @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                     @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                     @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
        productItems(model, keyword, pageNumber, pageSize, sortBy, sortOrder, COSMETICS);

        return "cosmetics";
    }
}
