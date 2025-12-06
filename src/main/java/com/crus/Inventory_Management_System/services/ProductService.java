package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    //ProductResponse getProductsByCategory(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductsByCategory(String categoryName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductByKeyword(String keyword);

    ProductResponse getProductByBarcodePartial(String barcode);

    void increaseProductQuantity(String username, Long productId) throws ResourceNotFoundException;

    ProductDTO getProductByQuantity(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws ResourceNotFoundException;

    ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException;

    ProductResponse getProductByKeywordAndBarcode(String keyword, String barcode);

    Product saveProduct(ProductDTO productDTO);

    ProductDTO getProductById(Long productId);

    void deleteItem(Long productId);

    ProductResponse getProductByKeywordAndCategory(String keyword, String allowedCategory);
}
