package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByVendorsId(Long id);
    Optional<Product> findByVendorsEmailAddress(String vendors_emailAddress);
    List<Product> findByVendorsContactName(String vendors_contactName);
    List<Product> findByVendorsPhoneNumber(Integer vendors_phoneNumber);
    List<Product> findByProductNameLikeIgnoreCase(String keyword);

    @Query("SELECT p FROM Product p JOIN p.categories c WHERE c = :category")
    List<Product> findProductsByCategory(Category category);

    List<Product> findProductByPrimaryBarcodeStartingWith(String primaryBarcode);

//    Product findProductsByProductId(Long productId);

}
