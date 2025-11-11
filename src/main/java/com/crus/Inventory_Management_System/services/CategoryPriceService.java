package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.mappers.CategoryPriceSummaryDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryPriceService {

    CategoryPriceSummaryDTO calculateCategoryTotalPrice(String categoryName);
}
