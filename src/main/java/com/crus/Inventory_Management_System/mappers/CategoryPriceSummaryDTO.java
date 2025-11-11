package com.crus.Inventory_Management_System.mappers;

import com.crus.Inventory_Management_System.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryPriceSummaryDTO {

    private String totalVbrp;
    private String totalVbcp;
    private Integer productCount;
    private String categoryName;

}
