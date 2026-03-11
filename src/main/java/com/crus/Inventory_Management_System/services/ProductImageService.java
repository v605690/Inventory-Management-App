package com.crus.Inventory_Management_System.services;

import com.crus.Inventory_Management_System.entity.Product;
import com.crus.Inventory_Management_System.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class ProductImageService {

    @Autowired
    ProductRepository productRepository;

    private final Path imageDir = Paths.get("product-images").toAbsolutePath();

    private String toSafeFileName(String productName) {
        if (productName == null || productName.isBlank()) {
            return "unknown-product";
        }
        String safeName = productName
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        return safeName.isBlank() ? "unknown-product" : safeName;
    }

    public void syncLocalImagesToDB() {
        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            String safeFile = toSafeFileName(product.getProductName());
            String safeFileName = toSafeFileName(safeFile)
                    + "-"
                    + product.getPrimaryBarcode()
                    + ".png";

            Path filePath = imageDir.resolve(safeFileName);

            if (Files.exists(filePath) && product.getImagePath() == null) {
                product.setImagePath(safeFileName);
                productRepository.save(product);
                System.out.println("🔗 Linked: " + product.getProductName());
            }
        }
    }
}
