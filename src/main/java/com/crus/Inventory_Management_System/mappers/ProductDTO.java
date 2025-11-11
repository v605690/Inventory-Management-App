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
    @JsonProperty("productName")
    private String productName;
    @JsonProperty("primaryBarcode")
    private String primaryBarcode;
    @JsonProperty("inStockQuantity")
    private Integer inStockQuantity;

    @JsonProperty("categories")
    private String categories;

    @JsonProperty("vbrp")
    private BigDecimal vbrp;
    @JsonProperty("vbcp")
    private BigDecimal vbcp;
}
