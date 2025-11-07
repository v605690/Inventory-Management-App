package com.crus.Inventory_Management_System.mappers;

import com.crus.Inventory_Management_System.entity.Category;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private Long id;
    private String accountNumber;
    private String productName;
    private String primaryBarcode;
    private Integer inStockQuantity;
    private Set<Category> categories = new HashSet<>();
    private BigDecimal vbrp;
    private BigDecimal vbcp;
}
