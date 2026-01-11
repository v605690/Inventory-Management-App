package com.crus.Inventory_Management_System.controllers;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.helpers.AccessHelper;
import com.crus.Inventory_Management_System.helpers.AppConstants;
import com.crus.Inventory_Management_System.helpers.DisplayKeywordTitle;
import com.crus.Inventory_Management_System.mappers.CategoryPriceDTO;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.mappers.ProductResponse;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import com.crus.Inventory_Management_System.services.CategoryPriceService;
import com.crus.Inventory_Management_System.services.ProductService;
import com.crus.Inventory_Management_System.services.ProductServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class ProductViewController {

    @Autowired
    ProductServiceImpl productServiceImpl;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryPriceService categoryPriceService;

    @Autowired
    private DisplayKeywordTitle displayKeywordTitle;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessHelper accessHelper;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/")
    public String viewIndexPage() {
        return "index";
    }

    @GetMapping("/products")
    public String viewHomePage(Model model,
                               @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                               @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                               @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                               @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder,
                               @RequestParam(name = "category", defaultValue = "all", required = false) String category) {

        ProductResponse productResponse;

        if (category == null || category.trim().isEmpty() || "null".equalsIgnoreCase(category) || "all".equalsIgnoreCase(category)) {
            category = "all";
            productResponse = productServiceImpl.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
        } else {
            productResponse = productServiceImpl.getProductByCategory(category, pageNumber, pageSize, sortBy, sortOrder);
        }

        final List<ProductDTO> productDTOList = productResponse.getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("totalPages", productResponse.getTotalPages());
        model.addAttribute("currentPage", productResponse.getPageNumber());
        model.addAttribute("totalElements", productResponse.getTotalElements());
        model.addAttribute("category", category);

        if ("all".equals(category)) {
            model.addAttribute("title", "HKM Product List");
        } else {
            model.addAttribute("title", category + " List");
        }

        return "products";
    }

    @GetMapping("/products/categories/{categoryName}")
    public String viewCategoryPage(Model model,
                                   @PathVariable String categoryName,
                                   @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                   @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                   @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                   @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder, Long userId) {

        ProductResponse productResponse = productServiceImpl.getProductsByCategory(categoryName, userId, pageNumber, pageSize, sortBy, sortOrder);

        List<ProductDTO> productDTOList = productResponse.getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", productResponse.getTotalPages());
        model.addAttribute("currentPage", productResponse.getPageNumber());
        model.addAttribute("totalElements", productResponse.getTotalElements());

        return "products";
    }

    @GetMapping("/products/keyword/{category}")
    public String searchProductByKeyword(@PathVariable String category,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                         @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                         @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                         @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY, required = false) String sortBy,
                                         @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder, Model model) {
        ProductResponse productResponse = productServiceImpl.getProductByKeywordAndCategory(keyword, category, pageNumber, pageSize, sortBy, sortOrder);
        final List<ProductDTO> productDTOList = productResponse.getContent();

        model.addAttribute("productDTOList", productDTOList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("totalPages", productResponse.getTotalPages());
        model.addAttribute("currentPage", productResponse.getPageNumber());
        model.addAttribute("totalElements", productResponse.getTotalElements());
        model.addAttribute("title", displayKeywordTitle.displayTitle(category, keyword));


        return "products";
    }

    @GetMapping("/graph")
    public String viewGraphPage(Model model, Pageable pageable) {
        // The code calls a method in the categoryPriceService to fetch a list of all categories
        // and their associated product counts, applying any pagination rules specified in the pageable argument.
        List<CategoryPriceDTO> categoryData = categoryPriceService.getAllCategoryPrices(pageable);

        // The code initializes a new list (graphData) designed to hold the data in a specific 2D format suitable
        // for a charting library (likely JavaScript on the front end). It iterates through the retrieved categoryData
        List<List<Object>> graphData = new ArrayList<>();

        if (categoryData != null) {
            // It retrieves the category name and count from each DTO
            for (CategoryPriceDTO dto : categoryData) {
                String name = dto.getCategoryName();
                int count = (dto.getProductCount() != null) ? dto.getProductCount() : 0;

                // It applies a filter: a category is only added to graphData if its product count is between 0 and 100,000 (inclusive).
                if (count >= 0 && count <= 100000) {
                    graphData.add(Arrays.asList(name, count));
                }
            }
        }
        // The prepared graphData is placed into the Model under the attribute name "chartData".
        // This makes the data accessible within the view template using that name.
        model.addAttribute("chartData", graphData);
        return "category";
    }

    @GetMapping("/meatPrice")
    public String viewMeatPricePage() {
        return "meatPrice";
    }

    @GetMapping("/overview")
    public String viewOverView() {
        return "overview";
    }

    @GetMapping("/new")
    public String showNewProductPage(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("product", productDTO);

        return "new-product";
    }

    @PostMapping(value = "/save")
    public String saveProduct(@ModelAttribute("product") ProductDTO productDTO, Model model) {

        Product product = modelMapper.map(productDTO, Product.class);

        Long userId = accessHelper.getLoggedInUserDetails();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userId));

        product.setUser(user);

        if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            model.addAttribute("message", "Product name cannot be empty");
            return "error";
        }

        ProductDTO savedProduct = productService.saveProduct(productDTO, userId);
        return "redirect:/edit/" + savedProduct.getId();
    }

    @GetMapping("/edit/{productId}")
    public String showEditProductPage(@PathVariable Long productId, Model model, HttpServletRequest request) throws ResourceNotFoundException {

        ProductDTO productDTO = productService.getProductById(productId);

        if (productDTO == null) {
            model.addAttribute("message", "Product not found");
            return "error";
        }
        model.addAttribute("product", productDTO);

        String referer = request.getHeader("Referer");
        model.addAttribute("returnUrl", referer);

        return "edit-product";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@PathVariable Long productId,
                                @RequestParam(name = "returnUrl", required = false) String returnUrl,
                                @ModelAttribute("product") ProductDTO productDTO, Model model) {

        Long userId = accessHelper.getLoggedInUserDetails();
        productDTO.setId(productId);

        if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            model.addAttribute("message", "Product name cannot be empty");
            return "edit-product";
        }

        productService.saveProduct(productDTO, userId);

        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }

        return "redirect:/products";
    }

    @GetMapping("/increase/{productId}")
    public String increaseQuantity(Model model, @PathVariable Long productId, Authentication authentication) {
        try {
            String username = authentication.getName();
            productServiceImpl.increaseProductQuantity(username, productId);
            return "redirect:/products";
        } catch (Exception e) {
            return "redirect:/products?error=true";
        }
    }

    @RequestMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId,
                                HttpServletRequest request) {

        productService.deleteItem(productId);

        String referer = request.getHeader("Referer");

        if (referer != null && !referer.isEmpty()) {
            return "redirect:" + referer;
        }

        return "redirect:/products/all";
    }
}
