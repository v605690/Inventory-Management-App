package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.entity.User;
import com.crus.Inventory_Management_System.entity.Vendor;
import com.crus.Inventory_Management_System.exceptions.APIException;
import com.crus.Inventory_Management_System.exceptions.ResourceNotFoundException;
import com.crus.Inventory_Management_System.helpers.AccessHelper;
import com.crus.Inventory_Management_System.mappers.ProductDTO;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import com.crus.Inventory_Management_System.repositories.UserRepository;
import com.crus.Inventory_Management_System.repositories.VendorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VendorServiceImpl implements VendorService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private AccessHelper accessHelper;


    @Override
    @Transactional
    public Vendor addVendor(Vendor vendor) {
        Vendor vendorList = modelMapper.map(vendor, Vendor.class);
        Optional<Vendor> vendorFromDB = vendorRepository.findVendorByAccountNumber(vendor.getAccountNumber());
        if (vendorFromDB != null) {
            throw new APIException("Vendor with account number " + vendor.getAccountNumber() + " already exists");
        }
        Vendor savedVendor = vendorRepository.save(vendorList);
        return modelMapper.map(savedVendor, Vendor.class);
    }

    @Override
    public List<Vendor> getAllVendors() {
      return vendorRepository.findAllWithProducts();
    }

    @Override
    public Vendor getVendor(Long vendorId) {

        Optional<Vendor> vendor = vendorRepository.findById(vendorId);
        return vendor.orElse(null);
    }

    @Override
    @Transactional
    public void deleteVendor(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Vendor", "vendorId", vendorId));

        vendorRepository.delete(vendor);
    }

    @Override
    @Transactional
    public void savedVendor(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    @Override
    @Transactional
    public Vendor updateVendor(Vendor vendor, String accountNumber) {
        Vendor savedVendor = vendorRepository.findById(vendor.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor", "id", vendor.getId()));

        savedVendor.setAccountNumber(vendor.getAccountNumber());
        savedVendor.setAddress(vendor.getAddress());
        savedVendor.setContactName(vendor.getContactName());
        savedVendor.setPhoneNumber(vendor.getPhoneNumber());
        savedVendor.setEmailAddress(vendor.getEmailAddress());

        savedVendor.getProducts().clear();
        if (vendor.getProducts() != null) {
            savedVendor.getProducts().addAll(vendor.getProducts());
        }

        return vendorRepository.save(savedVendor);
    }

    @Override
    public List<Vendor> saveAllVendor(List<Vendor> vendorList) {
        return List.of();
    }

    @Override
        public Vendor findVendorByAccountNumber(String accountNumber) {

        Vendor vendor = vendorRepository.findVendorByAccountNumber(accountNumber)
                .orElseThrow(() -> new APIException("Vendor not found"));

        vendor.getProducts().size();
        return vendor;
    }

    @Override
    public List<Product> searchProducts(String keyword) {

        return productRepository.findByProductNameContainingIgnoreCase(keyword);
    }

    @Transactional
    @Override
    public void associateProduct(Long vendorId, Long productId) {
        //Vendor vendor = getVendor(vendorId);
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new APIException("Vendor not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new APIException("Product not found"));

        if (product.getProductName() == null || product.getProductName().isBlank()) {
            product.setProductName("Unnamed Product # " + productId);
            productRepository.save(product);
        }

        vendor.getProducts().add(product);
        product.getVendors().add(vendor);

        vendorRepository.saveAndFlush(vendor);

        System.out.println("DEBUG: Vendor Products Size: " + vendor.getProducts().size());

    }

    @Transactional
    @Override
    public void createNewProductAndAssociate(Long id, ProductDTO product) {
        Product savedProduct = modelMapper.map(product, Product.class);

        Product saved = productRepository.save(savedProduct);

        Long userId = accessHelper.getLoggedInUserDetails();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        saved.setUser(user);

        associateProduct(id, saved.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Vendor findByAccountNumberWithProducts(String accountNumber) {
        Vendor vendor = vendorRepository.findByAccountNumberWithProducts(accountNumber)
                .orElseThrow(() -> new APIException("Vendor account number does not exist"));

        Hibernate.initialize(vendor.getProducts());
        return vendor;
    }
}
