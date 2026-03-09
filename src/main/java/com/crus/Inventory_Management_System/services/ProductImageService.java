package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    ProductRepository productRepository;

    private final Path imageDir = Paths.get("product-images").toAbsolutePath();

    private String generateHash(String productName) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(productName.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public void syncLocalImagesToDB() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            String hashFile = generateHash(product.getProductName());
            String fileName = hashFile + ".png";
            Path filePath = imageDir.resolve(fileName);

            if (Files.exists(filePath) && product.getImagePath() == null) {
                product.setImagePath(fileName);
                productRepository.save(product);
                System.out.println("🔗 Linked: " + product.getProductName());
            }
        }
    }
}
