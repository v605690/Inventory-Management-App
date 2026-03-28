package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface VendorService {
    Vendor addVendor(Vendor vendor);

    Page<Vendor> getAllVendors(String keyword, PageRequest pageRequest);

    Page<Vendor> getAllVendors(PageRequest pageRequest);

    Vendor getVendor(Long vendorId);
    
    void deleteVendor(Long vendorId);

    void savedVendor(Vendor vendor);

    Vendor updateVendor(Vendor vendor, String accountNumber);

    List<Vendor> saveAllVendor(List<Vendor> vendorList);

    Vendor findVendorByAccountNumber(String accountNumber);

    Page<Product> searchProducts(String keyword, PageRequest pageRequest);

    void associateProduct(Long vendorId, Long productId);

    void createNewProductAndAssociate(Long id, ProductDTO product);

    Vendor findByAccountNumberWithProducts(String accountNumber);
}
