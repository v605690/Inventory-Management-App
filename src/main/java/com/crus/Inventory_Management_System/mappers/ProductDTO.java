package com.crus.Inventory_Management_System.mappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;

    @JsonProperty("userId")
    private Long userId;

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
    private String imagePath;

    public boolean getId(String currentUsername) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return Objects.equals(id, that.id); // Compare by ID only
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
