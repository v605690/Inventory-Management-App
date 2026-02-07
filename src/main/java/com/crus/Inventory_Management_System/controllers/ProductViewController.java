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
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@RequestMapping("/products")
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

    @GetMapping()
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

    @GetMapping("/categories/{categoryName}")
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

    @GetMapping("/keyword/{category}")
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

    /**
     * Handles GET requests to display the new product page.
     * Initializes a new ProductDTO object and adds it to the model
     * for use in the view.
     *
     * The showNewProductPage method is annotated with @GetMapping annotation, this maps a GET request to /new URL
     *
     * FLOW:
     * User clicks Add Product -> which uses a GET request to /new URL of which -> showNewProductPage() executes
     * Where the method calls to display new-product.html form -> User fills form -> Submits ->
     * which uses a POST request to /save URL of which -> saveProduct() executes
     *
     * @param model the model object used to pass data to the view
     * @return the name of the view template for the new product page
     */
    @GetMapping("/new")
    public String showNewProductPage(Model model) {
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("product", productDTO);

        return "new-product";
    }

    /**
     * The @PostMapping annotation maps a POST request to /save URL; The @PostMapping ("/save") method is triggered
     * when a user submits the "Create New Product" form and saves the product details.  It is triggered by the
     * "Add Product" button, of which is noted in the @GetMapping ("/new") method.
     *
     * @param productDTO
     * @param model
     * @return
     */
    @PostMapping()
    public String saveProduct(@ModelAttribute("product") ProductDTO productDTO, Model model) {
        // Map ProductDTO to Product entity (converts the productDTO to an entity for JPA persistence)
        Product product = modelMapper.map(productDTO, Product.class);

        // Retrieve the logged-in user's details
        Long userId = accessHelper.getLoggedInUserDetails();
        // Fetch the user from the database or throw an exception if not found
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + userId));
        // Set the user for the product
        product.setUser(user);

        // Validate product name
        if (productDTO.getProductName() == null || productDTO.getProductName().isEmpty()) {
            model.addAttribute("message", "Product name cannot be empty");
            return "error";
        }

        // Save the product to the service layer; takes in DTO and userId for processing,
        // finally redirect to the edit page
        ProductDTO savedProduct = productService.saveProduct(productDTO, userId);
        return "redirect:/products/edit/" + savedProduct.getId();
    }
    // This method is not called, invoked by Spring’s DispatcherServlet when an HTTP request matches its route
    // Spring will call this method when the browser requests the edit page for a specific product,
    // it is also called via a redirect after creating a product; saveProduct redirects to edit page,
    // which triggers showEditProductPage
    @GetMapping("/edit/{productId}")
    public String showEditProductPage(@PathVariable Long productId, Model model, HttpServletRequest request) throws ResourceNotFoundException {

        ProductDTO productDTO = productService.getProductById(productId);

        if (productDTO == null) {
            model.addAttribute("message", "Product not found");
            return "error";
        }
        model.addAttribute("product", productDTO);
        model.addAttribute("productId", productId);

        String referer = request.getHeader("Referer");
        model.addAttribute("returnUrl", referer);

        return "edit-product";
    }

    /**
     * The @PostMapping annotation maps a POST request to /update/{productId} URL; The @PostMapping ("/update/{productId}")
     * method is triggered when a user submits the "Update Product" form and updates the product details,
     * method is called from the edit-product.html form.
     *
     * The form in the template has an action attribute: th:action="@{'/update/' + ${productId}}"`
     * When a user fills out the form and clicks the "Save" button, the form submits via HTTP POST to the endpoint
     * mapped by the @PostMapping ("/update/{productId}") annotation.
     *
     * FLOW:
     * User navigates to `/edit/{productId}` (handled by `showEditProductPage` method)
     * - The edit form loads with existing product data
     * - User modifies the product details and clicks "Save"
     * - Form submits to `/update/{productId}` → **this method executes**
     *
     * @param productId
     * @param returnUrl
     * @param productDTO
     * @param model
     * @return
     */

    @PostMapping("/update")
                                // Optional URL to redirect back to after update, return to the previous page
    public String updateProduct(@RequestParam(name = "returnUrl", required = false) String returnUrl,
                                // The product data from the form, automatically bound to a ProductDTO object
                                @Valid @ModelAttribute("product") ProductDTO productDTO,
                                // Used to check if the product DTO is valid and contains necessary data
                                BindingResult bindingResult,
                                Model model) {


        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Form validation failed. Please correct the highlighted fields.");
            return "edit-product";
        }
        // Retrieves the logged-in user's ID
        Long userId = accessHelper.getLoggedInUserDetails();
        if (userId == null) {
            model.addAttribute("message", "You must be logged in to update a product.");
            return "edit-product";
        }
        // Validate product, and product id is null; it adds an error message to the model
        if (productDTO == null || productDTO.getId() == null) {
            model.addAttribute("message", "Missing product id (cannot update).");
            return "edit-product";
        }
        // try catch block to handle exceptions during product update, takes a userId with productDTO
        try {
            productService.updateProduct(userId, productDTO);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("message", ex.getMessage());
            return "edit-product";
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("message", "Product not found (or you don't have access).");
            return "edit-product";
        }
        // Redirects back to the previous page if provided, otherwise redirects to the products page
        if (returnUrl != null && !returnUrl.isEmpty()) {
            return "redirect:" + returnUrl;
        }
        // Redirects to the products page
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
