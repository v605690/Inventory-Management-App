package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    ProductResponse getAllProducts();

    ProductResponse getProductsByCategory(String categoryName);

    ProductResponse getProductByKeyword(String keyword);

    ProductResponse getProductByBarcodePartial(String barcode);
}
