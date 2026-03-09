package com.crus.Inventory_Management_System.helpers;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String absolutePath = new File("product-images").getAbsolutePath();
        registry.addResourceHandler("/product-images/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
