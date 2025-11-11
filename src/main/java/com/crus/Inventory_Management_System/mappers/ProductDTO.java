package com.crus.Inventory_Management_System.mappers;

import com.crus.Inventory_Management_System.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Long id;
    @NotBlank(message = "Product name is required and cannot be empty")
    @JsonProperty("Product Name")
    private String productName;
    @JsonProperty("Primary Barcode")
    private String primaryBarcode;
    @JsonProperty("In Stock Quantity")
    private Integer inStockQuantity;
    @JsonProperty("Categories")
    private String categories;
    @JsonProperty("Valuation Based on Base Unit Retail($)")
    private BigDecimal vbrp;
    @JsonProperty("Valuation Based on Base Unit Cost($)")
    private BigDecimal vbcp;
}
