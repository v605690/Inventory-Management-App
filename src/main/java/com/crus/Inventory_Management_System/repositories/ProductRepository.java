package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByVendorsId(Long id);
    Optional<Product> findByVendorsEmailAddress(String vendors_emailAddress);
    List<Product> findByVendorsContactName(String vendors_contactName);
    List<Product> findByVendorsPhoneNumber(Integer vendors_phoneNumber);

    @Query(
            value = "SELECT p FROM Product p JOIN p.categories c WHERE c = :category",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories c WHERE c = :category"
    )
    Page<Product> findProductsByCategory(@Param("category") Category category, Pageable pageable);
    List<Product> findByProductNameLikeIgnoreCase(String keyword);
    List<Product> findProductByPrimaryBarcodeStartingWith(String primaryBarcode);
    List<Product> findByProductNameLikeIgnoreCaseAndPrimaryBarcodeStartingWith(String keyword, String barcode);

    ProductDTO findProductsById(Long id);

    void deleteProductById(Long id);
}
