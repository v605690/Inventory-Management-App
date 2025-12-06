package com.crus.Inventory_Management_System.services;
import com.crus.Inventory_Management_System.entity.Category;
import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    private Long convertUsernameToId(String username) {
        return Math.abs((long) username.hashCode());
    }

    @Override
    // Method implements addProduct interface method and takes a ProductDTO and returns a ProductDTO
    public ProductDTO addProduct(ProductDTO productDTO) {
        // Uses modelMapper to convert incoming ProductDTO to a product entity
        Product product = modelMapper.map(productDTO, Product.class);
        // Checks if the category field in DTO contains actual text (not null, empty, or just whitespace)
        // Splits the category string by commas
        // Trims whitespace from each category name
        // Parse each category string using categoryService.parseCategory() (Likely converts string to Category enum)
        // Collects results into a Set<Category> to avoid duplicates
        if (StringUtils.hasText(productDTO.getCategories())) {
            Set<Category> categorySet = Arrays.stream(productDTO.getCategories().split(","))
                    .map(String::trim)
                    .map(categoryService::parseCategory)
                    .collect(Collectors.toSet());
            // Debug to check for multiple categories
            if (categorySet.size() > 1) {
                System.out.println("Debug ");
            }
            // Assigns the processed category set to the product entity
            product.setCategories(categorySet);
        }

        Product savedProduct = productRepository.save(product);

        return convertToDTO(savedProduct);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort.Direction direction = switch (sortOrder.toLowerCase()) {
            case "desc" -> Sort.Direction.DESC;
            case "asc" -> Sort.Direction.ASC;
            default -> throw new IllegalArgumentException("Invalid sort order " + sortOrder);
        };

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        // Fetch all products using Data JPA findAll() method
        Page<Product> page = productRepository.findAll(pageable);

        // Uses stream to transform the list of Product entities
        List<ProductDTO> productDTOS = page.stream()
                // convertToDTO method handles the conversion from Product entity to ProductDTO
                .map(this::convertToDTO)
                .toList();

        // Create a ProductResponse object to wrap the list of DTOs

        return createProductResponse(productDTOS, page);
    }

    @Override
    public ProductResponse getProductsByCategory(String categoryName, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        // Takes the input categoryName (a String) and converts it to a Category object and
        // uses parseCategory method to handle the conversion
        Category category = categoryService.parseCategory(categoryName);
        // Uses Data JPA to find all products by category and returns a list of Product

        Sort.Direction direction = "desc".equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));

        Page<Product> page = productRepository.findProductsByCategoryName(category, pageable);

        // Transform a list of Product into ProductDTO objects using Java Streams
        List<ProductDTO> productDTOS = page.getContent()
                .stream()
                // Uses convertToDTO to convert each product
                .map(this::convertToDTO)
                .toList();
        // Create a ProductResponse wrapper object

        return createProductResponse(productDTOS, page);
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
    public ProductResponse getProductByKeywordAndCategory(String keyword, String allowedCategory) {
        Category category = categoryService.parseCategory(allowedCategory);

        List<Product> products = productRepository.findProductsByCategory(category, keyword);

        List<ProductDTO> productDTOS = products.stream()
                .filter(p -> !StringUtils.hasText(keyword) || (p.getProductName() != null && p.getProductName().toLowerCase().contains(keyword.toLowerCase())))
                .map((element) -> modelMapper.map(element, ProductDTO.class))
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
    public void increaseProductQuantity(String username, Long productId) throws ResourceNotFoundException {
        Long userId = convertUsernameToId(username);
        ProductDTO currentQuantity = getProductByQuantity(productId);

        // Add check to prevent NullPointerException
        if (currentQuantity == null) {
            throw new ResourceNotFoundException();
        }
        int quantity = currentQuantity.getInStockQuantity();
        quantity += 1;
        currentQuantity.setInStockQuantity(quantity);
        updateProduct(productId, currentQuantity);
    }

    @Override
    public ProductDTO getProductByQuantity(Long productId) {
        // Fixed: Implement actual retrieval instead of returning null
        return getProductById(productId);
    }

    @Override
    public ProductResponse getProductByKeywordAndBarcode(String keyword, String barcode) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCaseAndPrimaryBarcodeStartingWith('%' + keyword + '%', barcode);
        List<ProductDTO> productDTOS = products.stream().map((element) -> modelMapper.map(element, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    @Transactional
    public Product saveProduct(ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);

        if (StringUtils.hasText(productDTO.getCategories())) {
            Set<Category> categorySet = Arrays.stream(productDTO.getCategories().split(","))
                    .map(String::trim)
                    .map(categoryService::parseCategory)
                    .collect(Collectors.toSet());

            product.setCategories(categorySet);
        } else {
            product.setCategories(new HashSet<>());
        }
       return productRepository.save(product);
    }

    @Override
    public ProductDTO getProductById(Long productId) {
        return productRepository.findById(productId)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public void deleteItem(Long productId) {
        productRepository.deleteProductById(productId);
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) throws ResourceNotFoundException {
        // Use Spring Data JPA findbyId method to fetch the product by its productId
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);
        // Uses modelMapper to convert productDTO to Product entity
        Product product = modelMapper.map(productDTO, Product.class);
        // Manually set each field from the mapped product to the database product
        productFromDB.setProductName(product.getProductName());
        productFromDB.setPrimaryBarcode(product.getPrimaryBarcode());
        productFromDB.setInStockQuantity(product.getInStockQuantity());
        productFromDB.setCategories(product.getCategories());
        productFromDB.setVbrp(product.getVbrp());
        productFromDB.setVbcp(product.getVbcp());

        // Clear categories before it set to something else during the update
        productFromDB.getCategories().clear();
        if (productDTO.getCategories() != null && !productDTO.getCategories().isEmpty()) {
            Category category = Category.valueOf(productDTO.getCategories().toUpperCase());
            productFromDB.getCategories().add(category);
        }
        // Saved the updated product to the database
        // Maps the saved entity back to a DTO and returns it
        Product savedProduct = productRepository.save(productFromDB);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) throws ResourceNotFoundException {
        Product product = productRepository.findById(productId)
                .orElseThrow(ResourceNotFoundException::new);

        productRepository.delete(product);
        // Uses modelMapper to convert a product into a ProductDTO entity
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

    private ProductResponse createProductResponse(List<ProductDTO> productDTOS, Page<?> page) {
        ProductResponse productResponse = new ProductResponse();
        // Sets the converted DTO list as the content
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(page.getNumber());
        productResponse.setPageSize(page.getSize());
        productResponse.setTotalElements(page.getTotalElements());
        productResponse.setTotalPages(page.getTotalPages());
        productResponse.setLastPage(page.isLast());

        return productResponse;
    }
}
