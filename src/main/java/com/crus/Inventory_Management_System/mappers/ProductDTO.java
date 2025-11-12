package com.crus.Inventory_Management_System.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

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
