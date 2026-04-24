package com.cws.shop.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // Allow your frontend URL
        config.setAllowedOrigins(List.of("http://127.0.0.1:5500", "http://127.0.0.1:5501", "http://127.0.0.1:3000"));

        // Allow credentials (important for JWT / cookies)
        config.setAllowCredentials(true);

        // Allow all headers
        config.setAllowedHeaders(List.of("*"));

        // Allow all HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cache preflight response (optional)
        config.setMaxAge(3600L);

        // Apply to all APIs
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}