package com.crus.Inventory_Management_System.mappers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryPriceDTO {

    private String totalVbrp;
    private String totalVbcp;
    private Integer productCount;
    private String categoryName;

}
