package com.badena.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        String rutaFisica = "file:///D:/Sena/Badena/spring/marketplace/marketplace/uploads/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(rutaFisica);

    }
}