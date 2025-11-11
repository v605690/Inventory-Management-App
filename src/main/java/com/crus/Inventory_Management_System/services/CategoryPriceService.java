package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import org.springframework.stereotype.Service;

@Service
public interface CategoryPriceService {

    CategoryPriceDTO calculateCategoryTotalPrice(String categoryName);
}
