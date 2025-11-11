package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.config.AppConfig;
import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.mappers.CategoryPriceSummaryDTO;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class CategoryServicePriceImpl implements CategoryPriceService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private AppConfig appConfig;

    @Override
    public CategoryPriceSummaryDTO calculateCategoryTotalPrice(String categoryName) {
        Category category = appConfig.parseCategory(categoryName);
        List<Product> products = productRepository.findProductsByCategory(category);

        BigDecimal totalVbrp = calculateTotalVbrp(products);
        BigDecimal totalVbcp = calculateTotalVbcp(products);

        CategoryPriceSummaryDTO summaryDTO = new CategoryPriceSummaryDTO();
        summaryDTO.setCategoryName(categoryName);
        summaryDTO.setTotalVbrp(formatAsDollarAmount(totalVbrp));
        summaryDTO.setTotalVbcp(formatAsDollarAmount(totalVbcp));
        summaryDTO.setProductCount(products.size());

        return summaryDTO;
    }

    private String formatAsDollarAmount(BigDecimal amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(amount);
    }

    private BigDecimal calculateTotalVbrp(List<Product> products) {
        return products.stream()
                .map(Product::getVbrp)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalVbcp(List<Product> products) {
        return products.stream()
                .map(Product::getVbcp)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
