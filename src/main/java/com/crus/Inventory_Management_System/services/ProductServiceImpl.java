package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.config.AppConfig;
import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AppConfig appConfig;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {

        Product product = modelMapper.map(productDTO, Product.class);

        if (StringUtils.hasText(productDTO.getCategories())) {
            Set<Category> categorySet = Arrays.stream(productDTO.getCategories().split(","))
                    .map(String::trim)
                    .map(appConfig::parseCategory)
                    .collect(Collectors.toSet());
            product.setCategories(categorySet);
        }

        Product savedProduct = productRepository.save(product);

        return convertToDTO(savedProduct);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(this::convertToDTO)
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(String categoryName) {
        Category category = appConfig.parseCategory(categoryName);
        List<Product> products = productRepository.findProductsByCategory(category);

        List<ProductDTO> productDTOS = products.stream()
                .map(this::convertToDTO)
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse getProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');
        List<ProductDTO> productDTOS = products.stream().map((element) -> modelMapper.map(element, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse getProductByBarcodePartial(String barcode) {
        List<Product> products = productRepository.findProductByPrimaryBarcodeStartingWith(barcode);
        List<ProductDTO> productDTOS = products.stream().map((element) -> modelMapper.map(element, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws ResourceNotFoundException {

        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        Product product = modelMapper.map(productDTO, Product.class);
        productFromDB.setProductName(product.getProductName());
        productFromDB.setPrimaryBarcode(product.getPrimaryBarcode());
        productFromDB.setInStockQuantity(product.getInStockQuantity());
        productFromDB.setCategories(product.getCategories());
        productFromDB.setVbrp(product.getVbrp());
        productFromDB.setVbcp(product.getVbcp());

        Product savedProduct = productRepository.save(productFromDB);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        productRepository.delete(product);
        return modelMapper.map(product, ProductDTO.class);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        if (product.getCategories() != null && !product.getCategories().isEmpty()) {
            String categoriesString = product.getCategories().stream()
                    .map(Category::name)
                    .collect(Collectors.joining(", "));
            productDTO.setCategories(categoriesString);

        }
        return  productDTO;
    }
}
