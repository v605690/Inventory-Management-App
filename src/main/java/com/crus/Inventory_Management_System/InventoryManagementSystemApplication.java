package com.crus.Inventory_Management_System;

import com.crus.Inventory_Management_System.services.ProductImageService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryManagementSystemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementSystemApplication.class, args);
	}

	@Bean
	CommandLineRunner init(ProductImageService service) {
		return args -> {
			System.out.println("Checking for local images to sync...");
			service.syncLocalImagesToDB();
		};
	}

}
