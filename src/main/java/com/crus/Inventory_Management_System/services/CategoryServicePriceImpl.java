package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CategoryServicePriceImpl implements CategoryPriceService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Override
    public CategoryPriceDTO calculateCategoryTotalPrice(String categoryName, Pageable pageable) {
        Category category = categoryService.parseCategory(categoryName);
        Page<Product> page = productRepository.findProductsByCategoryName(category, pageable);
        List<Product> products = page.getContent();

        Double totalVbrp = calculateTotalVbrp(products);
        Double totalVbcp = calculateTotalVbcp(products);

        CategoryPriceDTO summaryDTO = new CategoryPriceDTO();
        summaryDTO.setCategoryName(categoryName);
        summaryDTO.setTotalVbrp(formatAsDollarAmount(totalVbrp));
        summaryDTO.setTotalVbcp(formatAsDollarAmount(totalVbcp));
        summaryDTO.setProductCount((int) page.getTotalElements());

        return summaryDTO;
    }

    @Override
    public List<CategoryPriceDTO> getAllCategoryPrices(Pageable pageable) {
        List<CategoryPriceDTO> result = new ArrayList<>();
        for (Category category : Category.values()) {
            try {
                result.add(calculateCategoryTotalPrice(category.name(), pageable));
            } catch (Exception ignored) {

            }
        }
        return result;
    }

    private String formatAsDollarAmount(Double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(amount);
    }

    private Double calculateTotalVbrp(List<Product> products) {
        double total = 0.0;

        for (Product product : products) {
            Double vbrp = product.getVbrp();

            if (vbrp != null) {
                total += vbrp;
            }
        }
        return total;
    }

    private Double calculateTotalVbcp(List<Product> products) {
        double total = 0.0;

        for (Product product : products) {
            Double vbcp = product.getVbcp();

            if (vbcp != null) {
                total += vbcp;
            }
        }

        return total;
    }
}
