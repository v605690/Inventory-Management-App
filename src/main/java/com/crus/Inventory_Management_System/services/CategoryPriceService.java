package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryPriceService {

    CategoryPriceDTO calculateCategoryTotalPrice(String categoryName, Pageable pageable);

    List<CategoryPriceDTO> getAllCategoryPrices(Pageable pageable);
}
