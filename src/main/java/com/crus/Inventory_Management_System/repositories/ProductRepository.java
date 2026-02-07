package com.crus.Inventory_Management_System.repositories;

import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    List<Product> findByVendorsPhoneNumber(String vendors_phoneNumber);

    @Query(
            value = "SELECT p FROM Product p JOIN p.categories c WHERE c = :category AND p.user.userId = :userId",
            countQuery = "SELECT COUNT(p) FROM Product p JOIN p.categories c WHERE c = :category AND p.user.userId = :userId"
    )
    Page<Product> findProductsByCategoryName(@Param("category") Category category, @Param("userId") Long userId, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE UPPER(p.productName) LIKE UPPER(:keyword) AND p.user.userId = :userId")
    Page<Product> findByKeywordAndUserId(@Param("keyword") String keyword, @Param("userId") Long userId, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCaseAndUser_UserId(@NotBlank(message = "Product name is required and cannot be empty") @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters") String productName, Long user_userId, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p JOIN p.categories c WHERE c = :category AND p.user.userId = :userId AND (LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.primaryBarcode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> findProductsByCategory(@Param("category") Category category, @Param("userId") Long userId, String keyword, Pageable pageable);

    List<Product> findProductByPrimaryBarcodeStartingWith(String primaryBarcode);
    List<Product> findByProductNameLikeIgnoreCaseAndPrimaryBarcodeStartingWith(String keyword, String barcode);

    void deleteProductById(Long id);
    
    Optional<Product> findByIdAndUser_UserId(Long id, Long userId);

    boolean existsByPrimaryBarcodeAndIdNot(String primaryBarcode, Long id);
}
