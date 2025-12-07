package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.config.AppConfig;
import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.crus.Inventory_Management_System.config.AppConfig.createProductResponse;

@Service
@Transactional
public class CategoryProductServiceImpl implements CategoryProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    @Override
    public ProductResponse getProductByKeywordAndCategory(String keyword, String allowedCategory, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryService.parseCategory(allowedCategory);

        Sort.Direction direction = switch (sortOrder.toLowerCase()) {
            case "desc" -> Sort.Direction.DESC;
            case "asc" -> Sort.Direction.ASC;
            default -> throw new IllegalArgumentException("Invalid sort order " + sortOrder);
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));

        Page<Product> page = productRepository.findProductsByCategory(category, keyword, pageable);

        List<ProductDTO> productDTOS = page.stream()
                .filter(p -> !StringUtils.hasText(keyword) || (p.getProductName() != null && p.getProductName().toLowerCase().contains(keyword.toLowerCase())))
                .map((element) -> modelMapper.map(element, ProductDTO.class))
                .toList();

        return createProductResponse(productDTOS, page);
    }
}
