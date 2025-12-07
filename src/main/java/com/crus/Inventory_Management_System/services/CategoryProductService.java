package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.mappers.ProductResponse;
import org.springframework.stereotype.Service;


public interface CategoryProductService {

    ProductResponse getProductByKeywordAndCategory(String keyword, String allowedCategory, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
